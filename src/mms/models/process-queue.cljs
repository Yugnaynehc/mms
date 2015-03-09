(ns mms.models.process-queue
  (:use
   [reagent.core :only [atom]]))

(defonce process-queue (atom (sorted-map)))
(defonce process-counter (atom 0))
(defonce process-addable (atom true))


(swap! process-queue assoc
       1 {:id 1 :size 100 :life 10}
       2 {:id 2 :size 200 :life 5})
(swap! process-counter inc)
(swap! process-counter inc)
