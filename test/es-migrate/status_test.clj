(ns es-migrate.status-test
  (:require [clojure.test :refer :all]
            [es-migrate.core :refer :all]
            [clojure.tools.logging :as log])
  (:use es-migrate.status))

;(def dsl1 (read-string (slurp "test-resources/migration1.edn")))
;
;(deftest simple-status
;  (testing "crud"
;    (is (= nil (get-status "001")))
;    (do
;      (start-progress dsl1)
;      (log/infof "State %s" @migration-state)
;      (is (= "running" (get-status "001")))
;      (stop-progress dsl1)
;      (log/infof "State %s" @migration-state)
;      (is (= "stopped" (get-status "001")))
;      )
;    ))
