(ns mms.controlers.app
  (:require
   [mms.models.app :as m]
   [mms.controlers.process-queue :as pro]
   [mms.controlers.section-table :as sec]))


(defn allocate-memory
  "为需要内存空间的进程分配空间"
  [process]
  ;; 如果有空闲分区
  (if-some [section (apply @m/allocate-algo [process])]
    section))

(defn swap-out-process
  "将某个进程置换出内存以此腾出空间"
  []
  (if-let [process (apply @m/swap-algo [])]
    (pro/suspend-process (:id process))))


(defn get-current-process-id
  "获得当前进程的id"
  []
  (deref m/current-process-id))

(defn set-current-process-id
  "设置当前进程"
  [pid]
  (reset! m/current-process-id pid))

(defn clean-current-process-id
  "清空当前进程记录"
  []
  (reset! m/current-process-id nil))

(defn get-next-process-id
  "获得接下来将要执行的进程的id"
  []
  (let [pid-coll (pro/get-process-queue-key)
        current-pid @m/current-process-id]
    (if-let [next-pid (first (filter #(> % current-pid) pid-coll))]
      next-pid
      (first pid-coll))))
