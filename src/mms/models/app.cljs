(ns mms.models.app
  (:use
   [reagent.core :only [atom]])
  (:require
   [mms.algo.allocate :as al]
   [mms.algo.swap :as sw]
   [mms.algo.mapping :as ma]))

(defonce allocate-algo
  ^{:doc "当前内存分配算法的记录"} (atom nil))

(defonce swap-algo
  ^{:doc "当前内存置换算法的记录"} (atom nil))

(defonce mapping-algo
  ^{:doc "当前内存映射算法的记录"} (atom nil))

(reset! allocate-algo al/first-fit)
(reset! swap-algo sw/first-find)

(defonce
  ^{:doc "记录当前运行进程的id"} current-process-id (atom nil))
