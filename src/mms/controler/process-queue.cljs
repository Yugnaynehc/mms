(ns mms.controler.process-queue
  (:require
   [mms.util :as u]
   [mms.models.process-queue :as m]))

(defn get-process-queue
  ""
  []
  (deref m/process-queue))

(defn add-process
  "将新产生的进程加入进程队列。
  需要提供的参数是内存大小：size，
  以及生命周期：life"
  [size life]
  (if (u/validate-string-num size life)
    (let [id (swap! m/process-counter inc)]
      (swap! m/process-queue assoc
             id {:id id :size size :life life}))))

(defn delete-process
  "删除一个进程"
  [id]
  (swap! m/process-queue dissoc id))
