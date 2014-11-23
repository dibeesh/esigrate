(ns nomad.upgrade-test
  (:require [clojure.test :refer :all]
            [nomad.core :refer :all]
            [clojure.tools.logging :as log])
  (:use nomad.upgrade
        nomad.common))


(def dsl {:users
          {:www_rcmmndr_com_9d39
           {:_all {:enabled false},
            :properties
                  {:text
                   {:type "multi_field",
                    :fields
                          {
                           :text {
                                  :type        "string",
                                  :term_vector "with_positions_offsets"
                                  },
                           :autocomplete
                                 {
                                  :type           "string",
                                  :term_vector    "with_positions_offsets",
                                  :index_analyzer "autocomplete",
                                  :include_in_all false}
                           }
                    },
                   :title
                   {:type "multi_field",
                    :fields
                          {:title {:type "string"},
                           :autocomplete
                                  {:type           "string",
                                   :term_vector    "with_positions_offsets",
                                   :boost          2.0,
                                   :index_analyzer "autocomplete",
                                   :include_in_all false}}},
                   :url
                   {:norms         {:enabled false},
                    :index_options "docs",
                    :type          "string",
                    :index         "not_analyzed"}}}}}
  )

(def upgraded-dsl {:users
                   {:www_rcmmndr_com_9d39
                    {:_all {:enabled false},
                     :properties
                           {:text
                            {:type        "string",
                             :term_vector "with_positions_offsets",
                             :fields
                                          {:autocomplete
                                           {:type           "string",
                                            :term_vector    "with_positions_offsets",
                                            :index_analyzer "autocomplete",
                                            :include_in_all false}}},
                            :title
                            {:type "string",
                             :fields
                                   {:autocomplete
                                    {:type           "string",
                                     :term_vector    "with_positions_offsets",
                                     :boost          2.0,
                                     :index_analyzer "autocomplete",
                                     :include_in_all false}}},
                            :url
                            {:norms         {:enabled false},
                             :index_options "docs",
                             :type          "string",
                             :index         "not_analyzed"}}}}})

(def field1 {:type "multi_field",
             :fields
                   {:text {:type "string"},
                    :autocomplete
                          {:type           "string",
                           :term_vector    "with_positions_offsets",
                           :index_analyzer "autocomplete",
                           :include_in_all false}}})

(def upgraded-field1 {:type "string",
                      :fields
                            {:autocomplete
                             {:type           "string",
                              :term_vector    "with_positions_offsets",
                              :index_analyzer "autocomplete",
                              :include_in_all false}}})


(def properties1
  {:_all {:enabled false},
   :properties
         {:text
          {:type "multi_field",
           :fields
                 {:text {:type "string"},
                  :autocomplete
                        {:type           "string",
                         :term_vector    "with_positions_offsets",
                         :index_analyzer "autocomplete",
                         :include_in_all false}}},
          :title
          {:type "multi_field",
           :fields
                 {:title {:type "string"},
                  :autocomplete
                         {:type           "string",
                          :term_vector    "with_positions_offsets",
                          :boost          2.0,
                          :index_analyzer "autocomplete",
                          :include_in_all false}}},
          :url
          {:norms         {:enabled false},
           :index_options "docs",
           :type          "string",
           :index         "not_analyzed"}}}
  )

(def upgraded-properties1 {:text
                           {:type "string",
                            :fields
                                  {:autocomplete
                                   {:type           "string",
                                    :term_vector    "with_positions_offsets",
                                    :index_analyzer "autocomplete",
                                    :include_in_all false}}},
                           :title
                           {:type "string",
                            :fields
                                  {:autocomplete
                                   {:type           "string",
                                    :term_vector    "with_positions_offsets",
                                    :boost          2.0,
                                    :index_analyzer "autocomplete",
                                    :include_in_all false}}},
                           :url
                           {:norms         {:enabled false},
                            :index_options "docs",
                            :type          "string",
                            :index         "not_analyzed"}})

(def single-upgraded-type {:_all {:enabled false},
                           :properties
                                 {:text
                                  {:type        "string",
                                   :term_vector "with_positions_offsets",
                                   :fields
                                                {:autocomplete
                                                 {:type           "string",
                                                  :term_vector    "with_positions_offsets",
                                                  :index_analyzer "autocomplete",
                                                  :include_in_all false}}},
                                  :title
                                  {:type "string",
                                   :fields
                                         {:autocomplete
                                          {:type           "string",
                                           :term_vector    "with_positions_offsets",
                                           :boost          2.0,
                                           :index_analyzer "autocomplete",
                                           :include_in_all false}}},
                                  :url
                                  {:norms         {:enabled false},
                                   :index_options "docs",
                                   :type          "string",
                                   :index         "not_analyzed"}}})

(deftest single-mfield
  (testing "upgrade"
    (is (= upgraded-field1
           (upgrade-single-mfield-type field1
                                       :text)))))

(deftest upgrade-properties
  (testing "upgrading-all-properties"
    (is (= upgraded-properties1
           (get-upgraded-properties properties1)
           ))))

(deftest upgrading-type
  (testing "upgrading-single-type"
    (is (= single-upgraded-type
           (upgrade-type (:www_rcmmndr_com_9d39 (:users dsl)))
           ))))



(deftest upgrade-all
  (testing "upgrading-all"
    (is (= upgraded-dsl
           (upgrade-all-types dsl "users"))
        )))