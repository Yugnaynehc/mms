(ns mms.controler.free-table
  (:require
   [jayq.core :refer [$]]
   [mms.util :as u]
   [mms.models.free-table :as m]))

(defn get-free-table
  ""
  []
  (deref m/free-table))

(defn update-free-table
  "更新空闲分区表"
  [])

(defn add-section
  "将新产生的空闲分区加入空闲分区表"
  [start end]
  (if (u/validate-string-num start end)
    (let [id (swap! m/section-counter inc)]
      (swap! m/free-table assoc
             id {:id id :start start :end end})
      (.html ($ :#model)
             (str "<h1>我现在有空间啦~~！ " (- end start) "MB哟~~</h1>")))))

(defn delete-section
  "删除一个分区"
  [id]
  (swap! m/free-table dissoc id))
