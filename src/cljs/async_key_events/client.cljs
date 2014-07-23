(ns async-key-events.client)

(defn ^:export init
  []
  (.write js/document "Hello, world!"))
