(ns mms.models.memory-model
  (:use
   [reagent.core :only [atom]]))

(defonce memory-size (atom nil))
