(ns ^:figwheel-always mms.core
    (:require
     [reagent.core :as reagent :refer [atom]]
     [reagent-modals.modals :as reagent-modals]
     [mms.util :as util]
     [jayq.core :as jq :refer [$]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defonce free-table (atom (sorted-map)))
(defonce section-counter (atom 0))
(defonce section-addable (atom true))

(defonce process-queue (atom (sorted-map)))
(defonce process-counter (atom 0))
(defonce process-addable (atom true))

;; (swap! free-table assoc
;;        1 {:id 1 :start 1 :end 100}
;;        2 {:id 2 :start 101 :end 200})
;; (swap! section-counter inc)
;; (swap! section-counter inc)

;; (swap! process-queue assoc
;;        1 {:id 1 :size 100 :life 10}
;;        2 {:id 2 :size 200 :life 5})
;; (swap! process-counter inc)
;; (swap! process-counter inc)

(defn hello-world []
  [:h1 (:text @app-state)])

(defn update-free-table
  "更新空闲分区表"
  [])

(defn add-modal
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
                           (.modal ($ :#reagent-modal) "hide"))}
     "保存"]]])

(defn add-section
  "将新产生的空闲分区加入空闲分区表"
  [start end]
  (if (util/validate-string-num start end)
    (let [id (swap! section-counter inc)]
      (swap! free-table assoc
             id {:id id :start start :end end})
      (.html ($ :#model)
             (str "我现在有空间啦~~！ " (- end start) "MB哟~~")))))

(def add-section-setting-props
  "添加新分区的表单参数"
  {:title "添加新分区"
   :description "设置新空闲分区的参数"
   :items [{:label "分区起始:" :id "sectionStart"}
           {:label "分区结束:" :id "sectionEnd"}]
   :create-on-click add-section})

(defn add-section-modal
  "添加分区的模态窗口控件，
  用来设置新增分区的参数"
  []
  [add-modal add-section-setting-props])

(defn add-process
  "将新产生的进程加入进程队列。
  需要提供的参数是内存大小：size，
  以及生命周期：life"
  [size life]
  (if (util/validate-string-num size life)
    (let [id (swap! process-counter inc)]
      (swap! process-queue assoc
             id {:id id :size size :life life}))))

(def add-process-setting-props
  "添加新进程的表单参数"
  {:title "添加新进程"
   :description "设置新进程的参数"
   :items [{:label "进程大小:" :id "processSize"}
           {:label "生命周期:" :id "processLife"}]
   :create-on-click add-process})

(defn add-process-modal
  "添加进程的模态窗口控件，
  用来设置新增进程的参数"
  []
  [add-modal add-process-setting-props])

(defn delete-process
  "删除一个进程"
  [id]
  (swap! process-queue dissoc id))

(defn delete-section
  "删除一个分区"
  [id]
  (swap! free-table dissoc id))

(defn table-item
  "空闲分区表的表项"
  []
  (let [editing (atom false)]
    (fn [index {:keys [id start end]}]
      [:tr
       [:td index]
       [:td start]
       [:td end
        [:span.destroy {:on-click #(delete-section id)}]]])))

(defn queue-item
  "进程队列的表项"
  []
  (let [editing (atom false)]
    (fn [index {:keys [id size life]}]
      [:tr
       [:td index]
       [:td size]
       [:td life
        [:span.destroy {:on-click #(delete-process id)}]]])))

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
      (doall (for [item (vals @free-table)]
               (do
                 (swap! index inc)
                 ^{:key (:id item)} [table-item @index item]))))]])

(defn free-table-did-mount
  "当空闲分区表控件成功挂载时，
  为它添加一些jQueryUI元素"
  []
  (.click ($ :#addSection)
          #(reagent-modals/modal! [add-section-modal] {:size :sm})))

(defn free-table-component
  "创建空闲分区表控件。"
  []
  (reagent/create-class {:render free-table-view
                         :component-did-mount free-table-did-mount}))


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
    (let [index (atom 0)]
      (doall (for [item (vals @process-queue)]
               (do
                 (swap! index inc)
                 ^{:key (:id item)} [queue-item @index item]))))]])

(defn process-queue-did-mount
  "当进程队列控件成功挂载时，
  为它添加一些jQuery UI元素"
  []
  (.click ($ :#addProcess)
          #(reagent-modals/modal! [add-process-modal] {:size :sm})))

(defn process-queue-component
  "创建进程队列控件"
  []
  (reagent/create-class {:render process-queue-view
                         :component-did-mount process-queue-did-mount}))

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

(defn app-view
  "这是整个应用的主界面，其它的部件都要挂载到此处"
  []
  [:h1 "main"]
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
  (.tooltip ($ "[data-toggle=\"tooltip\"]") #js {:track true})
  )

(defn app
  "完整的应用"
  []
  (reagent/create-class {:render app-view
                         :component-did-mount app-did-component}))


(reagent/render-component [app]
                          (. js/document (getElementById "app")))


