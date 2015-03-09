(ns mms.views.memory-model
  (:require
   [reagent.core :as reagent]))

(defn memory-model-view
  ""
  []
  [:div#model
   [:h1 "这里放模型"]])

(defn memory-model-did-mount
  ""
  [])

(defn memory-model-component
  ""
  []
  (reagent/create-class {:render memory-model-view
                         :component-did-mount memory-model-did-mount}))
