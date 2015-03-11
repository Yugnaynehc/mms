(ns mms.views.dashboard
  (:require
   [reagent.core :as reagent]
   [mms.algo.allocate :as allo]))

(defn dashboard-component
  ""
  []
  [:button {:on-click allo/first-fit} "next"])

