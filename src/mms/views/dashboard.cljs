(ns mms.views.dashboard
  (:require
   [reagent.core :as reagent]
   [mms.algo.allocate :as allo]
   [mms.controlers.dashboard :as c]))

(defn dashboard-component
  ""
  []
  [:div.btn-group {:class "text-center"}
   [:button {:on-click #(c/generate-process 5 20 10)
             :class "btn btn-default"}
    "生成随机进程"]
   [:button {:on-click c/next-state :class "btn btn-default"}
    "进程调度"]])

