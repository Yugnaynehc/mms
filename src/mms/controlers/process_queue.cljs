(ns mms.controlers.process-queue
  (:require
   [mms.util :as u]
   [mms.models.process-queue :as m]
   [mms.controlers.section-table :as sec]))

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
  (let [id (swap! m/process-counter inc)]
      (swap! m/process-queue assoc
             id {:id id :size size
                 :life life :state 0})))

(defn input-process
  "根据input框的输入来构造进程"
  [size life]
  (if (u/validate-string-num size life)
    (add-process (js/parseInt size)
                 (js/parseInt life))))

(defn delete-process
  "删除一个进程"
  [id]
  (swap! m/process-queue dissoc id)
  (sec/free-section id))

(defn choose-unloaded-process
  "挑选一个没有载入到内存中的进程"
  []
  (first (filter #(= 0 (:state %)) (get-process-queue-value))))

(defn load-process
  "更新进程队列中某个进程的状态,
  将进程加载到内存中"
  [path value]
  (swap! m/process-queue assoc-in path value))
