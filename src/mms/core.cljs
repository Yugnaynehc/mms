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

(defn tablie-item
  "空闲分区表的表项"
  []
  (let [editing (atom false)]
    (fn [{:keys {start end}}]
      [:li {:class (str (if @editing "editing"))}
       [:div.view
        [:label ]]])))

(defn free-table-component
  "空闲分区表的显示控件，以表格形式展现。
  暂时不可编辑。"
  []
  [:table {:class "table table-hover"}
   [:caption "空闲分区表"]
   [:thead
    [:tr
     [:th "序号"]
     [:th "起始"]
     [:th "结束"]]]
   [:tbody
    (for [item @free-table]
      (let [{:keys [id start end]} (second item)]
        [:tr
         [:td id]
         [:td start]
         [:td end]]))]])

(defn add-process
  "将新产生的进程加入进程队列。
  需要提供的参数是内存大小：size，
  以及生命周期：life"
  [{:keys [size life]}]
  (let [id (swap! process-counter inc)]
    (swap! process-queue assoc
           id {:id id :size size :life life})))

(defn update-free-table
  "更新空闲分区表"
  [])

(defn process-queue-component
  "进程队列的显示控件，以表格形式展现。
  需要实现增加进程，删除进程功能。
  增加进程时，需要设定的参数有：
  进程占用内存大小，进程生命周期"
  []
  [:table {:class "table table-hover"}
   [:caption "进程队列"]
   [:thead
    [:tr
     [:th "序号"]
     [:th "大小"]
     [:th "生命"]]]
   [:tbody
    (for [item @process-queue]
      (let [{:keys [id size life]} (second item)]
        [:tr
         [:td id]
         [:td size]
         [:td life]]))]])

(defn app
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

#_(defn app []
    [:div.col-md-5
     [edn->hiccup table-layout]])


(reagent/render-component [app]
                          (. js/document (getElementById "app")))


