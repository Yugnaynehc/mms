(ns mms.models.app
  (:use
   [reagent.core :only [atom]])
  (:require
   [mms.algo.allocate :as al]
   [mms.algo.recycle :as re]
   [mms.algo.mapping :as ma]))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))


(defonce allocate-algo
  ^{:doc "当前内存分配算法的记录"} (atom nil))

(defonce recycle-algo
  ^{:doc "当前内存回收算法的记录"} (atom nil))

(defonce mapping-algo
  ^{:doc "当前内存映射算法的记录"} (atom nil))

(set! allocate-algo al/first-fit)
