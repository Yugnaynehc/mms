(ns mms.views.section-table
  (:require
     [reagent.core :as reagent :refer [atom]]
     [jayq.core :as jq :refer [$]]
     [reagent-modals.modals :as reagent-modals]
     [mms.util :as u]
     [mms.controlers.section-table :as c]
     [mms.views.template :as t]))


(def add-section-setting-props
  "添加新分区的表单参数"
  {:title "添加新分区"
   :description "设置新空闲分区的参数"
   :items [{:label "分区起始" :id "sectionStart"}
           {:label "分区结束" :id "sectionEnd"}]
   :create-on-click c/add-section})

(defn add-section-modal-component
  "添加分区的模态窗口控件，
  用来设置新增分区的参数"
  []
  [t/add-modal-component add-section-setting-props])


(defn table-item
  "空闲分区表的表项"
  []
  (let [editing (atom true)]
    (fn [index {:keys [id pid start end state]}]
      [:tr
       [:td index]
       [:td (if pid (str "#" pid) "无")]
       [:td start]
       [:td end]
       (if state
         [:td.text-success "空闲"]
         [:td.text-primary "分配"])
       [:td {:style {:width "5%" :padding-left "0px"}}
        [:span.destroy {:on-click #(c/delete-section id)
                        :style {:display (if @editing
                                           "block"
                                           "none")}}]]])))

(def table-view-setting-props
  "构造分区表显示控件的参数"
  {:title "分区表"
   :button-id "freeTable"
   :on-click #(reagent-modals/modal! [add-section-modal-component] {:size :sm})
   :tip "添加一个新分区"
   :addable true
   :col [{:id 0 :text "序号"}
         {:id 1 :text "进程"}
         {:id 2 :text "起始"}
         {:id 3 :text "结束"}
         {:id 4 :text "状态"}]
   :values c/get-section-table-sorted-value 
   :item-component table-item})

(defn section-table-view
  "分区表的视图定义,以表格形式展现,
  内容暂时不可编辑。"
  []
  [t/table-component table-view-setting-props])

(defn section-table-did-mount
  "当分区表控件成功挂载时，
  为它添加一些jQueryUI元素"
  []
  )

(defn section-table-component
  "创建空闲分区表控件。"
  []
  (reagent/create-class {:render section-table-view
                         :component-did-mount section-table-did-mount}))
