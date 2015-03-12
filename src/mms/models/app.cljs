(ns mms.models.app
  (:use
   [reagent.core :only [atom]])
  (:require
   [mms.algo.allocate :as al]
   [mms.algo.recycle :as re]
   [mms.algo.mapping :as ma]))

(defonce allocate-algo
  ^{:doc "当前内存分配算法的记录"} (atom nil))

(defonce recycle-algo
  ^{:doc "当前内存回收算法的记录"} (atom nil))

(defonce mapping-algo
  ^{:doc "当前内存映射算法的记录"} (atom nil))

(reset! allocate-algo al/first-fit)

(defonce
  ^{:doc "记录当前运行进程的id"} current-process-id (atom nil))
