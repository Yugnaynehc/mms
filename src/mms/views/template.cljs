(ns mms.views.template
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent-modals.modals :as reagent-modals]
   [jayq.core :as jq :refer [$]]))


(defn add-modal-component
  "带表单的模态窗口控件模板"
  [props]
  [:div.modal-content
   [:div.modal-header
    [:button {:type "button" :class "close"
              :data-dismiss "modal" :aria-label "Close"}
     [:span {:aria-hidden true} "×"]]
    [:h4.modal-title (:title props)]]
   [:div.modal-body
    [:p (:description props)]
    [:form
     (for [item (:items props)
           :let [{:keys [label id]} item]]
       ^{:key id}
       [:div.form-group
        [:label label]
        [:input {:type "text" :class "form-control" :id id}]])]]
   [:div.modal-footer
    [:button {:type "button" :class "btn btn-default"
              :data-dismiss "modal"} "关闭"]
    [:button {:type "button" :class "btn btn-primary"
              :on-click (fn []
                           (apply (:create-on-click props)
                                  (map #(.-value %) ($ :input)))
                           (.modal ($ :#reagent-modal) "hide"))} "保存"]]])
