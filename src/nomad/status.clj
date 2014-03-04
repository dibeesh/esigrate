(ns nomad.status
  (:require [clojure.core.async :as async :refer :all]
            [nomad.es :as es])
  )

(def queue (chan 50)) ;50 migrations max


;simple status map like the following
;:id1 running
;:id2 finished
(def migration-state (atom {}))

(defn enqueue-progress [dsl]
  (reset! migration-state (assoc @migration-state (name (:id dsl)) "queued"))
  )

(defn start-progress [dsl]
  (reset! migration-state (assoc @migration-state (name (:id dsl)) "running"))
  )

(defn stop-progress [dsl]
  (reset! migration-state (assoc @migration-state (name (:id dsl)) "stopped"))
  )

(defn get-status [id]
  (get @migration-state (name id))
  )


(defn put-dsl-to-queue! [dsl]
  (go (>! queue dsl))
  (enqueue-progress dsl)
  )

(defn start-queue-processor! []
  (go
    (while true
      (let [dsl (<! queue)]
        (start-progress dsl)
        (dorun (es/exec-reindex-for-all-types! dsl))
        (stop-progress dsl)
        )
      )
    )
  )
