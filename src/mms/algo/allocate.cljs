(ns mms.algo.allocate
  (:require
   [mms.controlers.process-queue :as p]
   [mms.controlers.section-table :as s]))

(defn choose-initial-process
  "挑选一个没有载入到内存中的进程"
  []
  (first (filter #(= 0 (:state %)) (p/get-process-queue-value))))

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
  "首次适应算法，测试版本。
  后续需要传入进程，依据进程来调度"
  []
  (println "first-fit")
  (if-let [process (choose-initial-process)]
    (if-let [section (first (useable-section process))]
      (let [pid (:id process)
            require (:size process)
            sid (:id section)
            start (:start section)
            end (:end section)]
        (s/consume-section sid start end pid require)
        (p/load-process [pid :state] 2)))))

(defn best-fit
  "最佳适应算法"
  [])

(defn worst-fit
  "最坏适应算法"
  [])

