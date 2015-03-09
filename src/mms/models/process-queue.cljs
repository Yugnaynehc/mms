(ns mms.models.process-queue
  (:use
   [reagent.core :only [atom]]))

(defonce process-queue
  ^{:doc
    "进程队列的存储结构，每一项有4个字段：
     :id 序号
     :size 进程占用内存大小
     :life 进程生命周期
     :state 进程状态， true表示运行，false表示挂起
     后续还可以添加更多的属性字段和状态。"}
  (atom (sorted-map)))
(defonce process-counter (atom 0))
(defonce process-addable (atom true))


(swap! process-queue assoc
       1 {:id 1 :size 100 :life 10 :state false}
       2 {:id 2 :size 200 :life 5 :state true})
(swap! process-counter inc)
(swap! process-counter inc)
