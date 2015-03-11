(ns mms.controlers.section-table
  (:require
   [jayq.core :refer [$]]
   [mms.util :as u]
   [mms.models.section-table :as m]
   [mms.controlers.memory-model :as mem]
   [mms.controlers.process-queue :as pro]))

(defn get-section-table
  "获得section-table解引用后的值"
  []
  (deref m/section-table))

(defn get-section-table-value
  "获得section-table键值对的值部分"
  []
  (vals (deref m/section-table)))

(defn get-section-table-sorted-value
  "获得按照分区起始位置升序排列的分区表"
  []
  (sort-by #(:start %) (get-section-table-value)))

(defn add-memory
  "新建一块内存"
  [size]
  (when (u/validate-string-num size)
   (let [id (swap! m/section-counter inc)]
     (swap! m/section-table assoc
            id {:id id :pid nil :start 0
                :end (dec (js/parseInt size)) :state true}))
   (mem/set-memory-size size)))

(defn add-section
  "将新产生的空闲分区加入分区表"
  [start end]
  (let [id (swap! m/section-counter inc)]
    (swap! m/section-table assoc
           id {:id id :pid nil :start (js/parseInt start)
               :end (js/parseInt end) :state true})))

(defn delete-section
  "删除一个分区，如果分区内有进程存在，连同进程一起移除"
  [id]
  (let [section (get (get-section-table) id)
        pid (:pid section)]
    (swap! m/section-table dissoc id)
    (if-not (nil? pid)
      (pro/delete-process pid))))

(defn free-section
  "释放进程占用的内存空间"
  [pid]
  (let [target (first (filter #(= pid (:pid %)) (get-section-table-value)))
        id (:id target)]
    (when-not (nil? id)
      (swap! m/section-table assoc-in [id :pid] nil)
      (swap! m/section-table assoc-in [id :state] true))))

(defn consume-section
  "更新分区表，将一个空闲分区划分出
   一部分给新进程。"
  [id start end pid require]
  (let [boundary (dec (+ start require))
        new-id (swap! m/section-counter inc)]
    (swap! m/section-table assoc id
           {:id id :pid pid :start start
            :end boundary :state false})
    (if (< boundary end)
      (swap! m/section-table assoc
             new-id {:id new-id :pid nil :start (inc boundary)
                     :end end :state true}))))
