(ns ^:figwheel-always mms.core
    (:require
     [reagent.core :as reagent :refer [atom]]
     [json-html.core :refer [edn->hiccup]]
     [jayq.core :as jq :refer [$]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(def free-table (atom (sorted-map)))
(def index-counter (atom 0))

(def process-queue (atom (sorted-map)))
(def process-counter (atom 0))


(swap! free-table assoc
       1 {:id 1 :start 1 :end 100}
       2 {:id 2 :start 101 :end 200})
(swap! process-queue assoc
       1 {:id 1 :size 100 :life 10}
       2 {:id 2 :size 200 :life 5})

(def table-layout [[1 2] [3 4]])
(def table-item-layout nil)

(defn hello-world []
  [:h1 (:text @app-state)])

(defn update-free-table
  "更新空闲分区表"
  [])

(defn add-process
  "将新产生的进程加入进程队列。
  需要提供的参数是内存大小：size，
  以及生命周期：life"
  []
  (let [size (.-value ($ :#processSize))
        _ (println size)
        life (.-value ($ :#processLife))
        _ (println life)] 
    (if-not (or (nil? size) (nil? life))
      (let [id (swap! process-counter inc)]
        (swap! process-queue assoc
               id {:id id :size size :life life})))))

(defn table-item
  "空闲分区表的表项"
  []
  (let [editing (atom false)]
    (fn [{:keys [id start end]}]
      [:tr
       [:td id]
       [:td start]
       [:td end
        [:span.destroy]]])))

(defn queue-item
  "进程队列的表项"
  []
  (let [editing (atom false)]
    (fn [{:keys [id size life]}]
      [:tr
       [:td id]
       [:td size]
       [:td life
        [:span.destroy]]])))

(defn free-table-view
  "空闲分区表的视图定义,以表格形式展现,
   内容暂时不可编辑。"
  []
  [:table {:class "table table-hover"}
   [:caption "空闲分区表"
    [:span {:class "glyphicon glyphicon-plus"
            :data-toggle "tooltip"
            :title "添加一个新分区"}]]
   [:thead
    [:tr
     [:th "序号"]
     [:th "起始"]
     [:th "结束"]]]
   [:tbody
    (for [item (vals @free-table)]
      ^{:key (:id item)} [table-item item])]])

(defn free-table-did-mount
  "当空闲分区表控件成功挂载时，
  为它添加一些jQueryUI元素"
  []
  )

(defn free-table-component
  "创建空闲分区表控件。"
  []
  (reagent/create-class {:render free-table-view
                         :component-did-mount free-table-did-mount}))


(defn add-process-modal
  "添加进程时弹出的窗口控件，
  用来设置新增进程的参数"
  []
  [:div {:id "addProcessModal"
         :style {:display "none"}
         :title "添加新的进程"}
   [:p "设置添加新进程所需的参数"]
   [:form
    [:div.form-group
     [:label "进程大小:"]
     [:input {:class "form-control" :id "processSize"}]]
    [:div.form-group
     [:label "生命周期："]
     [:input {:class "form-control" :id "processLife"}]]]])

(defn process-queue-view
  "进程队列的视图，以表格形式展现。
  需要实现增加进程，删除进程功能。
  增加进程时，需要设定的参数有：
  进程占用内存大小，进程生命周期"
  []
  [:table {:class "table table-hover"}
   [:caption "进程队列"
    [:span {:class "glyphicon glyphicon-plus"
            :id "addProcess"
            :data-toggle "tooltip"
            :title "添加进程"}]]
   [:thead
    [:tr
     [:th "序号"]
     [:th "大小"]
     [:th "生命"]]]
   [:tbody
    (for [item (vals @process-queue)]
      ^{:key (:id item)} [queue-item item])]])

(defn process-queue-did-mount
  "当进程队列控件成功挂载时，
  为它添加一些jQuery UI元素"
  []
  (.click ($ :#addProcess)
          #(.dialog ($ :#addProcessModal)
                    #js {:modal true
                         :buttons
                         #js {:cancel (fn []
                                        (this-as t
                                                 (.dialog ($ t) "close")))
                              :create (fn []
                                        (this-as t
                                                 (add-process)
                                                 (.dialog ($ t) "close")))}})))

(defn process-queue-component
  "创建进程队列控件"
  []
  (reagent/create-class {:render process-queue-view
                         :component-did-mount process-queue-did-mount}))

(defn app-view
  "这是整个应用的主界面，其它的部件都要挂载到此处"
  []
  [:h1 "main"]
  [:div.container
   [:div.row
    [:div.col-md-3
     [free-table-component]
     [process-queue-component]]
    [:div.col-md-9
     [:h1 "这里放模型"]]]])

(defn app-did-component
  "当应用挂载到网页上时，加载一些设置"
  []
  (.tooltip ($ "[data-toggle=\"tooltip\"]") #js {:track true})
  )

(defn app
  "完整的应用"
  []
  (reagent/create-class {:render app-view
                         :component-did-mount app-did-component}))


(reagent/render-component [app]
                          (. js/document (getElementById "app")))


