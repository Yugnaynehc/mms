(ns mms.views.free-table
  (:require
     [reagent.core :as reagent :refer [atom]]
     [jayq.core :as jq :refer [$]]
     [reagent-modals.modals :as reagent-modals]
     [mms.util :as u]
     [mms.controler.free-table :as c]
     [mms.views.template :as t]))


(def add-section-setting-props
  "添加新分区的表单参数"
  {:title "添加新分区"
   :description "设置新空闲分区的参数"
   :items [{:label "分区起始:" :id "sectionStart"}
           {:label "分区结束:" :id "sectionEnd"}]
   :create-on-click c/add-section})

(defn add-section-modal-component
  "添加分区的模态窗口控件，
  用来设置新增分区的参数"
  []
  [t/add-modal-component add-section-setting-props])


(defn table-item
  "空闲分区表的表项"
  []
  (let [editing (atom false)]
    (fn [index {:keys [id start end]}]
      [:tr
       [:td index]
       [:td start]
       [:td end
        [:span.destroy {:on-click #(c/delete-section id)}]]])))


(defn free-table-view
  "空闲分区表的视图定义,以表格形式展现,
   内容暂时不可编辑。"
  []
  [:table {:class "table table-hover"}
   [:caption "空闲分区表"
    [:span {:class "glyphicon glyphicon-plus"
            :id "addSection"
            :data-toggle "tooltip"
            :title "添加一个新分区"}]]
   [:thead
    [:tr
     [:th "序号"]
     [:th "起始"]
     [:th "结束"]]]
   [:tbody
    (let [index (atom 0)]
      (doall (for [item (vals (c/get-free-table))]
               (do
                 (swap! index inc)
                 ^{:key (:id item)} [table-item @index item]))))]])

(defn free-table-did-mount
  "当空闲分区表控件成功挂载时，
  为它添加一些jQueryUI元素"
  []
  (.click ($ :#addSection)
          #(reagent-modals/modal! [add-section-modal-component] {:size :sm})))

(defn free-table-component
  "创建空闲分区表控件。"
  []
  (reagent/create-class {:render free-table-view
                         :component-did-mount free-table-did-mount}))
