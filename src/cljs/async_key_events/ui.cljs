(ns async-key-events.ui
  (:require [goog.events :as events]
            [cljs.core.async :refer [chan put!]])
  (:import [goog.events KeyHandler]
           [goog.events.KeyHandler]
           [goog.events.EventType]))

(def keyup-events
  (.-KEYUP events/EventType))

(def keydown-events
  (.-KEYDOWN events/EventType))

(def event-source
  (.-body js/document))

(defn listen
  ([event-type] 
   (listen event-type identity))
  ([event-type parse-event]
   (let [ev-chan (chan)]
     (events/listen (.-body js/document)
                    event-type
                    #(put! ev-chan (parse-event %)))
     ev-chan)))
   
