(ns mms.algo.swap
  (:require
   [mms.controlers.process-queue :as pro]))

(defn swapable-process
  "找出所有可以被置换出内存的进程"
  []
  (filter #(= 2 (:state %)) pro/get-process-queue-value))

(defn first-find
  "首次找到算法，找到第一个可被换出的进程"
  []
  (first (swapable-process)))
