(ns mms.models.free-table
  (:use
   [reagent.core :only [atom]]))

(defonce free-table (atom (sorted-map)))
(defonce section-counter (atom 0))
(defonce section-addable (atom true))

(swap! free-table assoc
       1 {:id 1 :start 1 :end 100}
       2 {:id 2 :start 101 :end 200})
(swap! section-counter inc)
(swap! section-counter inc)
