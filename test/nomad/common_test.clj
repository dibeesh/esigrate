(ns nomad.common-test
  (:require [clojure.test :refer :all]
            [nomad.core :refer :all]
            [clojure.tools.logging :as log])
  (:use nomad.common))

(deftest deep-keywordize-keys-test
  (testing "deep keywordize"
    (is (= {:index {:analysis {:analyzer {:comma {:type "pattern" :pattern ",\\s+"}}}}}
           (deep-keywordize-keys {:index.analysis.analyzer.comma.type "pattern", :index.analysis.analyzer.comma.pattern ",\\s+"})))))