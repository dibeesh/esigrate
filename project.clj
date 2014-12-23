(defproject esigrate "0.1.0-SNAPSHOT"
            :description "A Clojure application designed to Migrate/Copy Elasticsearch indices."
            :url "http://www.searchly.com"
            :license {:name "Apache License"
                      :url  "http://www.apache.org/licenses/LICENSE-2.0"}
            :dependencies [
                           [org.clojure/clojure "1.6.0"]
                           [org.clojure/core.async "0.1.346.0-17112a-alpha"]

                           [org.clojure/data.json "0.2.5"]

                           ;http layer
                           [http-kit "2.1.16"]
                           [compojure "1.3.1"]
                           [ring "1.3.2"]

                           ;logging
                           [org.clojure/tools.logging "0.3.1"]

                           ;;es related
                           [clojurewerkz/elastisch "2.1.0"]

                           ]
            :resource-paths ["resources" "test-resources"]
            :min-lein-version "2.0.0"
            :global-vars {*warn-on-reflection* true}
            :jvm-opts ["-Xmx1g" "-server"]
            :aot [esigrate.core] :main esigrate.core
            :tar {:uberjar true}
            :uberjar-name "esigrate.jar"
            )
