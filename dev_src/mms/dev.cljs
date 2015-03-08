(ns mms.dev
    (:require
     [mms.core]
     [figwheel.client :as fw]))

(fw/start {
  :websocket-url "ws://192.168.8.8:3449/figwheel-ws"
  :on-jsload (fn []
               ;; (stop-and-start-my app)
               )})
