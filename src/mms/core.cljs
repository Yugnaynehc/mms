(ns ^:figwheel-always mms.core
    (:require
     [reagent.core :as reagent :refer [atom]]
     [mms.views.app :as v]
     [jayq.core :as jq :refer [$]]))

(enable-console-print!)


(reagent/render-component [v/app]
                          (. js/document (getElementById "app")))


