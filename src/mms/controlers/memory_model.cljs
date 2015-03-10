(ns mms.controlers.memory-model
  (:require
   [mms.controlers.section-table :as sec]
   [mms.models.memory-model :as m]))

(defn get-section-table-length
  "获得分区表的项数"
  []
  (count (sec/get-section-table)))


(defn get-section-data
  "给定一个分区的起始和结束位置，
  以及这个分区内存放的进程，
  得到一个描述分区每一个单元的序列"
  [start end pid]
  (for [index (range start (inc end))]
    [(quot index 8) (mod index 8) pid]))


(defn get-memory-size
  "获得内存大小"
  []
  (deref m/memory-size))

(defn set-memory-size
  "设置内存大小"
  [size]
  (reset! m/memory-size size))

(defn get-memory-data
  "获得一个描述整个内存模型
  每一个单元的序列"
  []
  (reduce into []
          (for [item (sec/get-section-table-value)
                :let [{:keys [start end pid]} item]]
            (get-section-data start end pid))))

