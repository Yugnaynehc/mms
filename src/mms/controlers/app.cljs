(ns mms.controlers.app
  (:require
   [mms.models.app :as m]))

(defn allocate-memory
  "为需要内存空间的进程分配空间"
  []
  (apply m/allocate-algo []))
