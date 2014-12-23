(ns esigrate.env
  (:import (java.io PushbackReader))
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(defn load-props [filename]
  (with-open [r (io/reader filename)]
    (binding [*read-eval* false] (read (PushbackReader. r)))
    ))

(defn log-exec [f]
  (fn [& args]
    (let [res (apply f args)]
      (log/debug (str "Function: " f " Args: " args " Result: " res))
      res)))

(def props ((log-exec load-props) (-> "config.edn" io/resource)))
