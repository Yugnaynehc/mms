(ns mms.controlers.app
  (:require
   [mms.models.app :as m]
   [mms.controlers.process-queue :as pro]
   [mms.controlers.section-table :as sec]))


(defn allocate-memory
  "为需要内存空间的进程分配空间"
  [process]
  ;; 如果有空闲分区
  (if-some [section (apply m/allocate-algo [process])]
    (let [sid (:id section) start (:start section)
          end (:end section) pid (:id process)
          require (:size process)]
      (sec/consume-section sid start end pid require)
      (pro/load-process [pid :state] 2))
    (do
      (js/alert "没有空间啦~"))))

