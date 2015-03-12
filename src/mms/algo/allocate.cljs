(ns mms.algo.allocate
  (:require
   [mms.controlers.section-table :as s]))

(defn useable-section
  "给定一个进程，找出可以容纳该进程的所有内存分区"
  [process]
  (for [section (s/get-section-table-sorted-value)
        :when (true? (:state section))
        :let [start (:start section)
              end (:end section)
              free (inc (- end start))
              require (:size process)]]
    (if (>= free require)
      section)))

(defn first-fit
  "首次适应算法。
  传入进程，得到该进程将要载入的分区信息"
  [process]
  (println "first-fit")
  (if-let [section (first (useable-section process))]
    {:id (:id section)
     :start (:start section)
     :end (:end section)}))

(defn best-fit
  "最佳适应算法"
  [])

(defn worst-fit
  "最坏适应算法"
  [])

