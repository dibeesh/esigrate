(ns nomad.es
  (:require
    [clojurewerkz.elastisch.rest :as esr]
    [clojurewerkz.elastisch.rest.document :as doc]
    [clojurewerkz.elastisch.query :as q]
    [clojurewerkz.elastisch.rest.bulk :as bulk]
    [clojurewerkz.elastisch.rest.index :as idx]
    [clojure.tools.logging :as log] :reload-all)
  (:use nomad.common
        nomad.upgrade :reload-all))


;

(defn get-v1-document [doc]
  doc
  ;(-> % :_source :_source :_source :_source)
  )

(defn hits-from
  "Returns search hits from a response as a collection. To retrieve hits overview, get the :hits
   key from the response"
  [response]
  (get-in response [:hits :hits]))

(defn fetch-scroll-results
  [scroll-id results]
  (let [scroll-response (doc/scroll scroll-id :scroll "1m")
        hits (hits-from scroll-response)]
    (if (seq hits)
      (recur (:_scroll_id scroll-response) (concat results hits))
      (concat results hits))))

;reindex a single type in a specific index to destination
(defn reindex-single-type! [dsl type]
  "get destination from dsl
  reindex specified index and type"
  ;;get old mapping
  ;;create new mapping

  (let [src-cli (esr/connect (-> dsl :src :url))
        src-seq (binding [clojurewerkz.elastisch.rest/*endpoint* src-cli]
                  (let [response (doc/search (-> dsl :src :index) type :query (q/match-all) :search_type "query_then_fetch" :scroll "1m" :size 1000 :fields ["_source", "_routing", "_parent"])
                        initial-hits (hits-from response)
                        scroll-id (:_scroll_id response)
                        all-hits (fetch-scroll-results scroll-id initial-hits)]
                    all-hits
                    )
                  )
        dest-cli (esr/connect (-> dsl :dest :url))]
    (log/infof "First Doc to migrate %s" (first src-seq))
    (log/debugf "Countted: %s" (count src-seq))
    (if-not (nil? (first src-seq))
      (binding [clojurewerkz.elastisch.rest/*endpoint* dest-cli]
        (let [ops (bulk/bulk-index (map #(assoc
                                            (:_source (get-v1-document %))
                                          :_id
                                          (:_id (get-v1-document %))
                                          :_parent
                                          (->> (get-v1-document %)
                                               :fields
                                               :_parent))
                                        src-seq))]
          (log/infof "First BULK OP to execute %s" (first ops))
          ;(with-redefs-fn {#'clojurewerkz.elastisch.rest/post-string
          ;                  (fn [^String uri & {:keys [body] :as options}]
          ;                    (io! (cheshire.core/decode (:body (clj-http.client/post uri (merge options {:accept :json :body body :throw-entire-message? true}))) true)))}
          ;                #(bulk/bulk-with-index-and-type (-> dsl :dest :index) type ops)
          ;                )
          (bulk/bulk-with-index-and-type (-> dsl :dest :index) type ops)
          ;(map #(println (-> % :_source :_source :_source :_source :_source)) src-seq)
          )))))


(defn get-src-index-mappings [dsl]
  (let [src-cli (esr/connect (-> dsl :src :url))]
    (binding [clojurewerkz.elastisch.rest/*endpoint* src-cli]
      (let [index-mapping (get (idx/get-mapping (-> dsl :src :index)) (keyword (-> dsl :src :index)))]
        ;(log/infof "Index Mapping: %s" index-mapping)
        (if (contains? index-mapping :mappings)
          ;v1 and legacy difference
          (:mappings index-mapping)
          index-mapping)))))


(defn get-src-type-mapping [dsl type]
  (get (get-src-index-mappings dsl) (keyword type)))

(defn exec-reindex-for-all-types! [dsl]
  ;get all types
  ;for all create the mapping first
  ;then reindex
  (log/infof "Start Migrating for dsl  %s..." dsl)
  (let [src-mappings (get-src-index-mappings dsl)
        src-types (keys src-mappings)]
    (for [type src-types]
      (let [src-mapping (get src-mappings type)
            src-cli (esr/connect (-> dsl :src :url))
            all-settings (binding [clojurewerkz.elastisch.rest/*endpoint* src-cli]
                           (idx/get-settings (-> dsl :src :index)))
            src-settings (:settings ((keyword (-> dsl :src :index)) all-settings))
            safe-settings (dissoc src-settings :index.routing.allocation.include.tag :index.uuid :index.number_of_replicas)
            dest-cli (esr/connect (-> dsl :dest :url))]
        (log/infof "Start Migrating source type %s..." type)
        (log/infof "Creating destination index  %s if it does not exist" (-> dsl :dest :index))
        (log/infof "UNSAFE Settings %s" src-settings)
        (log/infof "SAFE Settings %s" safe-settings)
        (binding [clojurewerkz.elastisch.rest/*endpoint* dest-cli]
          (when-not (idx/exists? (-> dsl :dest :index))
            (idx/create (-> dsl :dest :index) :settings safe-settings))

          (log/infof "Updating Mapping for index %s and type %s and mapping %s..." (-> dsl :dest :index) type src-mapping)
          (if (mapping-needs-upgrade? src-mapping)
            ;;type needs upgrade, change dsl manually and push the new one
            (let [up-mapping (upgrade-type src-mapping)]
              (log/infof "Upgraded mapping because it contained multi type, Url %s type %s new-mapping %s"
                         (-> dsl :dest :index) (name type) {(keyword type) up-mapping})
              (let [upgrade-result (idx/update-mapping (-> dsl :dest :index)
                                                       (name type)
                                                       :mapping
                                                       {(keyword type) up-mapping}
                                                       :ignore_conflicts false)]
                (log/infof "UPGRADE RESULT %s " upgrade-result)))
            ;;does not need upgrade, copy the type without modifications
            (let [upgrade-result (idx/update-mapping (-> dsl :dest :index) (name type) :mapping
                                                     {(keyword type) src-mapping} :ignore_conflicts false)]
              (log/infof "UPGRADE RESULT %s " upgrade-result)))


          (when
              (idx/type-exists? (-> dsl :dest :index) (name type))
            (log/infof "Start Reindexing source type %s..." type)
            (reindex-single-type! dsl (name type))
            (log/infof "Reindexing of source type %s... finished" type)))))))

(comment
  (<> (exec-reindex-for-all-types! (clojure.data.json/read-str (slurp "test-resources/parent.json") :key-fn keyword)))
  )