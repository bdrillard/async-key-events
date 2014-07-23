(defproject async-key-events "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2069"]
                 [ring "1.2.0"]
                 [domina "1.0.2"]
                 [org.clojure/core.async "0.1.242.0-44b1e3-alpha"]]
  :plugins [[lein-cljsbuild "1.0.0"]
            [lein-ring "0.8.7"]]
  :hooks [leiningen.cljsbuild]
  :source-paths ["src/clj"]
  :main async-key-events.server
  :ring {:handler async-key-events.server/app}
  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/client.js"
                           :optimizations :advanced}}]})
