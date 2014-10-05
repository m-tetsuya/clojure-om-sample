(defproject myapp "0.1.0-SNAPSHOT"
            :description "FIXME: write this!"
            :url "http://example.com/FIXME"

            :dependencies [
                           [org.clojure/clojure "1.6.0"]
                           [org.clojure/clojurescript "0.0-2311"]
                           [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                           [om "0.7.1"]
                           [compojure "1.0.1"]
                           [liberator "0.10.0"]
                           [ring/ring-jetty-adapter "1.1.0-SNAPSHOT"]
                           [ring/ring-devel "1.1.0-SNAPSHOT"]
                           [korma "0.3.0-RC6"]
                           [mysql/mysql-connector-java "5.1.13"]
                           ]

            :plugins [
                      [lein-ring "0.8.8"]
                      [lein-cljsbuild "1.0.4-SNAPSHOT"]
                      ]

            :source-paths ["src/clj"]

            :ring {:handler myapp.core/handler}

            :cljsbuild { 
                        :builds [{:id "myapp"
                                  :source-paths ["src/cljs"]
                                  :compiler {
                                             :output-to "resources/public/myapp.js"
                                             :output-dir "resources/public/out"
                                             :optimizations :none
                                             :source-map true}}]})
