(ns mms.controlers.dashboard
  (:require
   [mms.controlers.app :as app]
   [mms.controlers.process-queue :as pro]))


(defn next-state
  "计算下一个状态"
  []
  ;; 如果有未载入的进程
  (if-let [process (pro/choose-unloaded-process)]
    (app/allocate-memory process)))


(defn generate-process
  "产生(0, n]个随机进程，
  每个进程的大小范围为(0, size],
  生命周期范围为(0, life]"
  [n size life]
  (let [num (inc (rand-int n))]
    (doseq [id (range num)]
      (pro/add-process (inc (rand-int size))
                       (inc (rand-int life))))))
