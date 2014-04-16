(ns nomad.upgrade
  (:use nomad.common)
  (:require [clojure.tools.logging :as log]
            [clojure.walk :as w]))

(defn upgrade-single-mfield-type [root field]
  "upgrades a type container such as:
  :title {

  }

  returns an upgraded version of the speicifed field only
  http://www.elasticsearch.org/guide/en/elasticsearch/reference/1.x/_multi_fields.html
  "
  (if (= "multi_field" (:type root))
    (let [actual-type (get-in root [:fields field :type])
          ;put it to top
          root1 (assoc root :type actual-type)
          root2 (dissoc-in root1 [:fields field :type])
          ]

      ;(log/infof "Field %s" field)
      ;(log/infof "Root %s" root)
      ;(log/infof "Root1 %s" root1)
      ;(log/infof "Root2 %s" root2)
      root2)
    (do
      (log/infof "Container does not contain multi_field, no modifications")
      root)))

(defn get-upgraded-properties [type-root]
  "gets an old mapping with multi_fields and return an updated one"
  ;remove multi_field
  ;remove redundant reference
  (log/debugf "get-upgraded-properties %s" type-root)
  (let [properties (find-first-key type-root :properties)
        fields (keys properties)
        modified-fields (into [] (map #(upgrade-single-mfield-type (find-first-key type-root %) %) fields))]
    ;(log/infof "found %s types to modify" fields)
    ;(log/infof "first modified field:%s" (first modified-fields))
    (zipmap fields modified-fields)))

;;for all types upgrade the properties and collect
(defn upgrade-type [type-root]
  (log/debugf "upgrade-type %s" type-root)
  (let [upgraded-properties (get-upgraded-properties type-root)]
    (assoc type-root :properties upgraded-properties)))

(defn upgrade-all-types [mapping index-name]
  (log/debugf "upgrade-all-types mapping: %s index-name %s" mapping index-name)
  (let [types (keys (first (vals mapping)))
        upgraded-types (into [] (map #(upgrade-type (find-first-key mapping %)) types))]
    {(keyword index-name) (zipmap types upgraded-types)}))

(defn mapping-needs-upgrade? [mapping]
  (.contains (str mapping) "multi_field"))
