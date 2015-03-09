(ns mms.controlers.section-table
  (:require
   [jayq.core :refer [$]]
   [mms.util :as u]
   [mms.models.section-table :as m]))

(defn get-section-table
  "返回section-table解引用后的值"
  []
  (deref m/section-table))

(defn get-section-table-value
  ""
  []
  (vals (deref m/section-table)))

(defn get-section-table-sorted-value
  ""
  []
  (sort-by #(:start %) (get-section-table-value)))

(defn add-section
  "将新产生的空闲分区加入分区表"
  [start end]
  (if (u/validate-string-num start end)
    (let [id (swap! m/section-counter inc)]
      (swap! m/section-table assoc
             id {:id id :pid nil :start (js/parseInt start)
                 :end (js/parseInt end) :state true})
      (.html ($ :#model)
             (str "<h1>我现在有空间啦~~！ " (- end start) "MB哟~~</h1>")))))

(defn delete-section
  "删除一个分区"
  [id]
  (swap! m/section-table dissoc id))

(defn update-section-table
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
             new-id {:id new-id :pid pid :start (inc boundary)
                     :end end :state true}))))
