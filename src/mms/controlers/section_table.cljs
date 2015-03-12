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

#_(defn merge-section
  "合并连续的空闲分区，
  以第一个分区为起始，第二个分区为结束"
  [first-section second-section]
  (let [id (:id first-section)
        start (:start first-section)
        end (:end second-section)]
    {:id id :pid nil :start start
     :end end :state true}))

(defn delete-section
  "删除一个分区，如果分区内有进程存在，连同进程一起移除"
  [id]
  (let [section (get (get-section-table) id)
        pid (:pid section)]
    (swap! m/section-table dissoc id)
    (if-not (nil? pid)
      (pro/delete-process pid))))

(defn free-section
  "释放进程占用的内存空间，
  并且更新分区表"
  [pid]
  (let [sorted-table (get-section-table-sorted-value)
        [pre target aft] (first (filter
                                 #(= pid (:pid (second %)))
                                 (partition 3 1
                                            (concat [nil] sorted-table [nil]))))
        id (:id target)
        _ (println id)
        pre-state (:state pre)
        aft-state (:state aft)]
    (when-not (nil? id)
      (cond
        ;; 如果之前和之后的分区都是空闲的
        (every? true? [pre-state aft-state])
        (let [pre-id (:id pre)
              aft-id (:id aft)
              new-end (:end aft)]
          (swap! m/section-table assoc-in [pre-id :end] new-end)
          (swap! m/section-table dissoc id)
          (swap! m/section-table dissoc aft-id))
        ;; 如果之前的分区是空闲的
        (and (true? pre-state)
             (not= true aft-state))
        (do 
          (swap! m/section-table assoc-in [(:id pre) :end] (:end target))
          (swap! m/section-table dissoc id))
        ;; 如果之后的分区是空闲的
        (and (not= true pre-state)
             (true? aft-state))
        (do
          (swap! m/section-table assoc-in [id :end] (:end aft))
          (swap! m/section-table assoc-in [id :pid] nil)
          (swap! m/section-table assoc-in [id :state] true)
          (swap! m/section-table dissoc (:id aft)))
        ;; 前后分区都不空闲
        :else
        (do
          (swap! m/section-table assoc-in [id :pid] nil)
          (swap! m/section-table assoc-in [id :state] true))))))

(defn consume-section
  "更新分区表，将一个空闲分区划分出
   一部分给新进程。"
  [id start end pid require]
  (let [boundary (dec (+ start require))]
    (swap! m/section-table assoc id
           {:id id :pid pid :start start
            :end boundary :state false})
    ;; 如果有剩余，就会产生一个新的空闲分区
    (if (< boundary end)
      (let [new-id (swap! m/section-counter inc)]
       (swap! m/section-table assoc
              new-id {:id new-id :pid nil :start (inc boundary)
                      :end end :state true})))))

