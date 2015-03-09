(ns mms.controlers.section-table
  (:require
   [jayq.core :refer [$]]
   [mms.util :as u]
   [mms.models.section-table :as m]))

(defn get-section-table
  "返回section-table解引用后的值"
  []
  (deref m/section-table))

(defn update-section-table
  "更新分区表"
  [])

(defn add-section
  "将新产生的空闲分区加入分区表"
  [start end]
  (if (u/validate-string-num start end)
    (let [id (swap! m/section-counter inc)]
      (swap! m/section-table assoc
             id {:id id :start start :end end :state true})
      (.html ($ :#model)
             (str "<h1>我现在有空间啦~~！ " (- end start) "MB哟~~</h1>")))))

(defn delete-section
  "删除一个分区"
  [id]
  (swap! m/section-table dissoc id))
