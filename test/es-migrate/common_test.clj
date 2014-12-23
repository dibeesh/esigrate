(ns es-migrate.common-test
  (:require [clojure.test :refer :all]
            [es-migrate.core :refer :all]
            [clojure.tools.logging :as log])
  (:use es-migrate.common))

(deftest deep-keywordize-keys-test
  (testing "deep keywordize"
    (is (= {:index {:analysis {:analyzer {:comma {:type "pattern" :pattern ",\\s+"}}}}}
           (deep-keywordize-keys {:index.analysis.analyzer.comma.type "pattern", :index.analysis.analyzer.comma.pattern ",\\s+"})))))