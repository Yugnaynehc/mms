(ns mms.models.process-queue
  (:use
   [reagent.core :only [atom]]))

(defonce process-queue
  ^{:doc
    "进程队列的存储结构，每一项有4个字段：
     :id 序号
     :sid 对应内存分区的id
     :size 进程占用内存大小
     :life 进程生命周期
     :state 进程状态， 0表示初始，1表示运行，2表示就绪，3表示挂起
     后续还可以添加更多的属性字段和状态。"}
  (atom (sorted-map)))
(defonce process-counter (atom 0))
(defonce process-addable (atom true))


;; (swap! process-queue assoc
;;        1 {:id 1 :size 100 :life 10 :state 0}
;;        2 {:id 2 :size 200 :life 5 :state 1})
;; (swap! process-counter inc)
;; (swap! process-counter inc)
