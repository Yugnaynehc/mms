(ns mms.views.dashboard
  (:require
   [reagent.core :as reagent]
   [mms.algo.allocate :as allo]
   [mms.controlers.dashboard :as c]))

(defn dashboard-component
  ""
  []
  [:div {:class "btn-group center"}
   [:a {:on-click #(c/generate-random-memory 128 256)
        :class "btn btn-default"}
    "随机生成内存"]
   [:a {:on-click #(c/generate-random-process 5 80 10)
        :class "btn btn-default"}
    "随机生成进程"]
   [:a {:on-click c/next-model-state :class "btn btn-default"}
    "进程调度"]
   [:a {:on-click c/clean-model-state :class "btn btn-danger"}
    "清除数据"]])

