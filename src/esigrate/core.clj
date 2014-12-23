(ns esigrate.core
  (:use esigrate.rest
        esigrate.status)
  (:gen-class))

(defn -main []
  (start-queue-processor!)
  (init-rest-server!))
