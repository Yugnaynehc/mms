(ns mms.views.app
  (:require
   [reagent.core :as reagent]
   [jayq.core :refer [$]]
   [reagent-modals.modals :as reagent-modals])
  (:use
   [mms.views.free-table :only [free-table-component]]
   [mms.views.process-queue :only [process-queue-component]]
   [mms.views.memory-model :only [memory-model-component]]))

(defn app-view
  "这是整个应用的主界面，其它的部件都要挂载到此处"
  []
  [:div.container
   [:div.row
    [:div.col-md-3
     [reagent-modals/modal-window]
     [free-table-component]
     [process-queue-component]]
    [:div.col-md-9
     [memory-model-component]]]])

(defn app-did-component
  "当应用挂载到网页上时，加载一些设置"
  []
  (.tooltip ($ "[data-toggle=\"tooltip\"]") #js {:track true}))

(defn app
  "完整的应用"
  []
  (reagent/create-class {:render app-view
                         :component-did-mount app-did-component}))
