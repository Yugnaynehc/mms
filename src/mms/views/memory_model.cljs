(ns mms.views.memory-model
  (:require
   [reagent.core :as reagent]
   [jayq.core :refer [$]]))

(def chart-config
  ""
  {:data {:csv (.html ($ :#csv))}
   :chart {:type "heatmap"
           :inverted true
           :backgroundColor nil}
   :credits {:enabled false}
   :title {:text "Map" :align "left"}
   :xAxis {:tickPixelInterval 50
           :min (.UTC js/Date 2015 3 1)
           :max (.UTC js/Date 2015 3 30)}
   :yAxis {:title {:text nil}
           :labels {:format "{value}:00"}
           :minPadding 0
           :maxPadding 0
           :startOnTick false
           :endOnTick false
           :tickPositions [0 6 12 18 24]
           :tickWidth 1
           :min 0
           :max 23}
   :colorAxis {:stops [[0   "#3060cf"]
                       [0.5 "#fffbbc"]
                       [0.9 "#c4463a"]]
               :min -5}
   :series [{:borderWidth 0
             :colsize: (* 24 1)
             :tooltip {:headerFormat "Temperature<br/>"
                       :pointFormat "{point.x:%e %b, %Y} {point.y}:00: <b>{point.value} ℃</b>"}}]
   })

(defn memory-model-view
  ""
  []
  [:div#model
   [:h1 "这里放模型"]])

(defn memory-model-did-mount
  ""
  []
  (js/$ #(.highcharts ($ :#model) (clj->js chart-config))))


(defn memory-model-component
  ""
  []
  (reagent/create-class {:render memory-model-view
                         :component-did-mount memory-model-did-mount
                         :component-did-update memory-model-did-mount}))
