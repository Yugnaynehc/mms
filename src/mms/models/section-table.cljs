(ns mms.models.section-table
  (:use
   [reagent.core :only [atom]]))

(defonce section-table
  ^{:doc
    "分区表的存储结构，每一项有4个字段
     :id 序号
     :pid 对应进程id
     :start 分区起始位置
     :end 分区结束位置
     :state 分区状态，true表示空闲，false表示已分配"}
  (atom (sorted-map)))
(defonce section-counter (atom 0))
(defonce section-addable (atom true))

(swap! section-table assoc
       1 {:id 1 :pid nil :start 1 :end 100 :state false}
       2 {:id 2 :pid nil :start 101 :end 200 :state true})
(swap! section-counter inc)
(swap! section-counter inc)
