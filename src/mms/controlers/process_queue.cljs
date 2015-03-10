(ns mms.controlers.process-queue
  (:require
   [mms.util :as u]
   [mms.models.process-queue :as m]))

(defn get-process-queue
  "获得process-queue解引用后的值"
  []
  (deref m/process-queue))

(defn get-process-queue-value
  "获得process-queue中每一个键值对的值部分"
  []
  (vals (deref m/process-queue)))

(defn add-process
  "将新产生的进程加入进程队列。
  需要提供的参数是内存大小：size，
  以及生命周期：life"
  [size life]
  (if (u/validate-string-num size life)
    (let [id (swap! m/process-counter inc)]
      (swap! m/process-queue assoc
             id {:id id :size (js/parseInt size)
                 :life (js/parseInt life) :state 0}))))

(defn delete-process
  "删除一个进程"
  [id]
  (swap! m/process-queue dissoc id))

(defn update-process-queue
  "更新进程队列中某个进程的状态"
  [path value]
  (swap! m/process-queue assoc-in path value))
