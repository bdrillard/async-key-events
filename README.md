# async-key-events

A decidedly small project demonstrating proper usage of [ClojureScript](http://clojure.org/clojurescript) to gather in-browser keyboard events and insert them into a [core.async](https://github.com/clojure/core.async/) channel.

## Usage and Notes

You may be just getting into ClojureScript and concurrency, and you'd like to understand how to gather keyboard events to do something with later. There are plenty of great examples of code out there, but this one has been deliberately drilled down to the bare minimum to show how you can gather an event, put it in a channel, and then reference it from elsewhere in your code base. To get started with this demo, run
```
lein ring server
```
This will compile our ClojureScript to JavaScript and open a browser session where you can test out what we'll be looking at. 

First, let's talk about Google's Closure Library, specifically, its methods for handling [events](https://developers.google.com/closure/library/docs/events_tutorial). ClojureScript has the Closure library builtin, so we can make use of all kinds of builtin goodies. In particular, we're interested in their **events/listen** function, defined as:
```
goog.events.listen(eventSource, eventType, listener, [capturePhase, [handler]]);
```
Listener events are dispatched in two phases, first is capture, where the event listener is attached to the target DOM element where we want to gather our events from. The second phase is bubbling, where captured event objects travel back up and are acted upon. So structurally, what we will do is attached to the Body of the HTML page an event listener that fires off an asynchronous 'put' to a thread every time we press down a key. So let's look at the real business logic of the our listen function:
```
(defn listen
  ([event-type] 
    (listen event-type identity))
  ([event-type parse-function]
    (let [ev-chan (chan)]
      (events/listen (.-body js/document)
                      event-type
                      #(put! ev-chan (parse-function %)))
      ev-chan)))
```
Let's say we have two use cases. On one hand, we may just want to capture the whole event object and not really do anything special to parse it. On another hand, we might write a special function that translates a key event to a particular Clojure keyword, perhaps to represent commands. To support both cases, we take advantage of Clojure's defn [arity](http://clojure.org/functional_programming), which can quite nicely allow one function to behave differently if differing numbers of arguments are provided. 

Looking up at our **listen** function, if we only provide the event-type of event we're looking for, it call will **listen** again but with the [identity](http://clojuredocs.org/clojure_core/clojure.core/identity) function as a second argument. So on this second call, we do three things. First, we use the *let* form to bind a core.async channel to a name. Second, we use the Google Closure **events/listen** function, which takes our document body as its first argument (the location on the DOM where we'll listen for events), it takes our provided event-type (down key presses in our case), and our all-important *listener* function, providing *identity* as our parse-event function. events/listen will now deploy a listener on the DOM that will capture our bubbled-up event, and shove it just as it is into our channel by evaluating the event with the identity function. The last thing we do in our listen function is return back the event-channel to our calling environment, so that in our async loop, we can bind the channel and draw from it. 

If we wanted to parse our captured-and-bubbled event object a bit before shoving it into the channel, we could have passed an optional parse-function, which arity would resolve correctly. To maintain our functional properties, our **listen** workhorse function takes in an event-type to listen to and returns a channel, a very clear 1-to-1 relationship. But the function also has the *effect* of binding a listener function to th DOM that captures keyboard events and inserts them into our channel. Thankfully, this single non-functional aspect is housed cleanly by the core.async api, allowing us to handle our keyboard events (or any other type of event we'd like) concisely and safely, allowing us elegant concurrency.

This turns out to be quite a demonstrative example of expressive Clojure -- using some builtin nuances in arity and identity, as well as powerful DOM interaction through Google Closure inclusion, and also being a showcase of ClojureScript and core.async.
