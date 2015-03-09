(ns mms.models.app
  (:use
   [reagent.core :only [atom]]))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))
