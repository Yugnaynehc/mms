(ns mms.views.dashboard
  (:require
   [reagent.core :as reagent]
   [mms.algo.allocate :as allo]
   [mms.controlers.dashboard :as c]))

(defn dashboard-component
  ""
  []
  [:button {:on-click c/allocate-memory} "next"])

