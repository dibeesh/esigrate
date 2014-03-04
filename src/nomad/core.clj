(ns nomad.core
  (:use nomad.rest
        nomad.status))

(defn -main []
  (start-queue-processor!)
  (init-rest-server!))
