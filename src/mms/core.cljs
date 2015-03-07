(ns ^:figwheel-always mms.core
    (:require
     [reagent.core :as reagent :refer [atom]]
     [jayq.core :as jq :refer [$]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn hello-world []
  [:h1 (:text @app-state)])



(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))


