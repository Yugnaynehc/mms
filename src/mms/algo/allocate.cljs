(ns mms.algo.allocate
  (:require
   [mms.controlers.process-queue :as p]
   [mms.controlers.section-table :as s]))

(defn choose-process
  "挑选一个没有载入到内存中的进程"
  []
  (first (filter #(= 0 (:state %)) (p/get-process-queue-value))))

(defn first-fit
  "首次适应算法"
  []
  (if-let [process (choose-process)]
    (for [section (s/get-section-table-value)
          :when (true? (:state section))
          :let [section-id (:id section)
                start (:start section)
                end (:end section)
                free (inc (- end start))
                process-id (:id process)
                require (:size process)]]
      (if (>= free require)
        (do
          (s/update-section-table section-id start end process-id require)
          (p/update-process-queue [process-id :state] 2))))))

(defn best-fit
  "最佳适应算法"
  [])

(defn worst-fit
  "最坏适应算法"
  [])

