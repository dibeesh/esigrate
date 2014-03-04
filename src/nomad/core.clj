(ns nomad.core
  (:use nomad.rest))

(defn -main []
  (init-rest-server!))
