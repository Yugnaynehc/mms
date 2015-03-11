(ns mms.views.memory-model
  (:require
   [reagent.core :as reagent :refer [atom]]
   [jayq.core :refer [$]]
   [mms.controlers.memory-model :as c]))

(defn memory-tip
  "提供内存单元的详细信息"
  []
  (this-as t
           (let [x (.. t -point -x)
                 y (.. t -point -y)
                 pid (.. t -point -value)
                 address (+ (* x 8) y)]
             (str "<b>内存地址: </b>" address "<br>"
                  "<b>所在进程: </b>" (if (nil? pid) "无" (str "#" pid))))))

(defn get-chart-config
  ""
  []
  (clj->js
   {:chart {:type "heatmap"
            :inverted true
            :backgroundColor nil}
    :credits {:enabled false}
    :title {:text nil}
    :xAxis {:allowDecimals false
            :min 0
            :max (quot (c/get-memory-size) 8)}
    :yAxis {:categories ["chip 0" "chip 1" "chip 2" "chip 3"
                         "chip 4" "chip 5" "chip 6" "chip 7"]
            :title nil
            :labels {:autoRotation false}}
    :tooltip {:formatter memory-tip
              :hideDelay 0
              :shared true
              :enabled true}
    :colorAxis {:min 0
                :minColor "#FFFFFF"
                :maxColor (first (.. js/Highcharts getOptions -colors))}
    :legend {:align "right"
             :layout "vertical"
             :verticalAlign "top"
             :y 25
             :symbolHeight 280}
    
    :series [{:name "Sales per employee"
              :borderWidth 0.5
              :data (c/get-memory-data)
              :dataLabels {:enabled false
                           :color "#000000"}}]
    }))

(defn memory-model-view
  ""
  []
  [:div#model
   (if (= 0 (c/get-section-table-length))
     [:div.well
      [:h1 "暂无内容"]
      [:p "请添加一块内存区域"]])]) 

(defn memory-model-will-update
  ""
  []
  (.remove ($ :.highcharts-container)))

(defn memory-model-did-update
  ""
  []
  (if-not (= 0 (c/get-section-table-length))
    (.highcharts ($ :#model) (get-chart-config))))

#_(defn memory-model-did-update
  ""
  []
  (let [chart (.highcharts ($ :#model))
        series (first (.series chart))]
    (.setData series (c/get-memory-data))))

(defn memory-model-will-unmount
  ""
  []
  (.remove ($ :.highcharts-container)))

(defn memory-model-component
  ""
  []
  (reagent/create-class {:render memory-model-view
                         :component-will-update memory-model-will-update
                         :component-did-update memory-model-did-update
                         :component-will-unmount memory-model-will-unmount}))
