(ns mms.views.process-queue
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent-modals.modals :as reagent-modals]
   [mms.util :as u]
   [jayq.core :as jq :refer [$]]
   [mms.controler.process-queue :as c]
   [mms.views.template :as t]))


(def add-process-setting-props
  "添加新进程的表单参数"
  {:title "添加新进程"
   :description "设置新进程的参数"
   :items [{:label "进程大小:" :id "processSize"}
           {:label "生命周期:" :id "processLife"}]
   :create-on-click c/add-process})

(defn add-process-modal-component
  "添加进程的模态窗口控件，
  用来设置新增进程的参数"
  []
  [t/add-modal-component add-process-setting-props])

(defn queue-item
  "进程队列的表项"
  []
  (let [editing (atom false)]
    (fn [index {:keys [id size life]}]
      [:tr
       [:td index]
       [:td size]
       [:td life
        [:span.destroy {:on-click #(c/delete-process id)}]]])))

(def process-queue-setting-props
  "构造进程队列显示控件的参数"
  {:title "进程队列"
   :on-click #(reagent-modals/modal! [add-process-modal-component] {:size :sm})
   :tip "添加进程"
   :col [{:id 1 :text "序号"}
         {:id 2 :text "大小"}
         {:id 3 :text "周期"}]
   :values c/get-process-queue 
   :item-component queue-item})

(defn process-queue-view
  "进程队列的视图，以表格形式展现。
  需要实现增加进程，删除进程功能。
  增加进程时，需要设定的参数有：
  进程占用内存大小，进程生命周期"
  []
  [t/table-component process-queue-setting-props])

(defn process-queue-did-mount
  "当进程队列控件成功挂载时，
  为它添加一些jQuery UI元素"
  []
  (.click ($ :#addProcess)
          #(reagent-modals/modal! [add-process-modal-component] {:size :sm})))

(defn process-queue-component
  "创建进程队列控件"
  []
  (reagent/create-class {:render process-queue-view
                         :component-did-mount process-queue-did-mount}))
