(defproject nomad "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                  [org.clojure/clojure "1.5.1"]

                  [org.clojure/data.json "0.2.4"]

                  ;http layer
                  [http-kit "2.1.5"]
                  [compojure "1.1.3"]
                  [ring/ring-core "1.1.8"]


                  ;logging
                  [org.clojure/tools.logging "0.2.6"]


                  ;;es related
                  [clojurewerkz/elastisch "1.5.0-beta1"]

                  ;;redis related
                  [com.taoensso/carmine "2.4.6"]

                  ]
  :resource-paths ["resources" "test-resources"]
  :min-lein-version "2.0.0"
  :global-vars {*warn-on-reflection* true}
  :jvm-opts ["-Xmx1g" "-server"]
  :aot [nomad.core] :main nomad.core

  )
