(ns mms.views.template
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent-modals.modals :as reagent-modals]
   [jayq.core :as jq :refer [$]]))


(defn add-modal-component
  "带表单的模态窗口控件模板"
  [props]
  [:div.modal-content
   [:div {:class "modal-header bg-info"}
    [:button {:type "button" :class "close"
              :data-dismiss "modal" :aria-label "Close"}
     [:span {:aria-hidden true} "×"]]
    [:h4.modal-title (:title props)]]
   [:div.modal-body
    [:h4 (:description props)]
    [:form
     (for [item (:items props)
           :let [{:keys [label id default]} item]]
       ^{:key id}
       [:div.form-group
        [:label label]
        [:input {:type "text" :default-value default
                 :class "form-control" :id id}]])]]
   [:div.modal-footer
    [:button {:type "button" :class "btn btn-default"
              :data-dismiss "modal"} "关闭"]
    [:button {:type "button" :class "btn btn-primary"
              :on-click (fn []
                          (apply (:create-on-click props)
                                 (map #(.-value %) ($ :input)))
                          (.modal ($ :#reagent-modal) "hide"))} "保存"]]])

(defn table-component
  "动态表格控件模板"
  [props]
  (let [items (apply (:values props) [])]
    [:div {:class "panel panel-primary" }
     [:div.panel-heading (:title props)
      (if (or (:addable props) (zero? (count items)))
        [:span {:class "glyphicon glyphicon-plus"
                :id (:button-id props)
                :on-click (:on-click props)
                :data-toggle "tooltip"
                :title (:tip props)}])]
     #_[:div.panel-body]
     [:table {:class "panel table table-hover"}
      [:thead
       [:tr
        (for [col (:col props)]
          ^{:key (:id col)} [:th (:text col)])
        [:th {:style {:width "5%" :padding-left "0px"}}
         [:span.destroy {:style {:visibility "hide"}}]]]]
      [:tbody
       (let [index (atom 0)]
         (doall (for [item items]
                  (do
                    (swap! index inc)
                    ^{:key (:id item)} [(:item-component props)
                                        @index item]))))]]]))
