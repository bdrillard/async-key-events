(ns async-key-events.client
  (:require [async-key-events.ui :as ui]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn ^:export init
  []
  (.write js/document "Open up a JavaScript console and press some keys!")
  (let [input (ui/listen ui/keydown-events)]
    (go-loop []
      (.log js/console (.-keyIdentifier (.-event_ (<! input))))
      (recur))))
