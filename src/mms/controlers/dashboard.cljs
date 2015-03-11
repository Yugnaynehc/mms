(ns mms.controlers.dashboard
  (:require
   [mms.controlers.app :as app]))

(defn allocate-memory
  "为需要内存空间的进程分配空间,
  直接调用mms.controlers.app中的
  allocate-memory"
  []
  (app/allocate-memory))
