(ns esigrate.common)

;http://www.pitheringabout.com/?p=678
(defn find-key [ks k m]
  (cond
    (map? m)
    (reduce into (map (partial conj ks) (filter #{k} (keys m)))
            (map #(find-key (conj ks (key %)) k (val %)) m))
    (vector? m)
    (reduce into '() (map #(find-key (conj ks %1) k %2)
                          (iterate inc 0) m))))

(defn find-first-key [coll k]
  (get-in coll (first (find-key [] k coll))))

;from contrib
(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

(defmacro <> [stmnt]
  (try stmnt (catch Throwable t (.printStackTrace t))))


(defn- deep-merge [& maps]
  (if (every? map? maps)
    (apply merge-with deep-merge maps)
    (last maps)))

(defn- str-keys-to-map [[k v]]
  (let [ks (map keyword (filter not-empty (clojure.string/split (name k)  #"[\.]")))]
    (when-not (empty? ks) (assoc-in {} ks v))))

(defn deep-keywordize-keys [m]
  (->> m (map str-keys-to-map) (apply deep-merge)))
