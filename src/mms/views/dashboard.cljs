(ns mms.views.dashboard
  (:require
   [reagent.core :as reagent]
   [mms.algo.allocate :as allo]
   [mms.controlers.dashboard :as c]))

(defn dashboard-component
  ""
  []
  [:div
   [:button {:on-click c/next-state} "下一步"]
   [:button {:on-click #(c/generate-process 5 20 10)} "生成随机进程"]])

