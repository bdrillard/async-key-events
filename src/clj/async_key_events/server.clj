;;;; Author: Aleksander Eskilson
;;;; Ring server entry for app
(ns async-key-events.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :refer (wrap-resource)]
            [ring.middleware.content-type :refer (wrap-content-type)]
            [ring.util.response :as response])
  (:gen-class))

(defn handler [uri]
  (if (not= "/index.html" uri)
    (response/redirect "/index.html")))

(def app
  (-> handler
      (wrap-content-type)
      (wrap-resource "public")))

(defn -main [& args]
  (jetty/run-jetty app {:port 3031}))
