(ns frontend.components.common
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer close!]]
            [frontend.async :refer [put!]]))

;; XXX flashes
(defn flashes []
  "")

(def spinner
  [:svg {:viewBox "0 0 100 100"
         :spinner ""
         :dangerouslySetInnerHTML
         (clj->js
          {"__html" "<path fill=\"#fff\" d=\"M50 0c-23.297 0-42.873 15.936-48.424 37.5-.049.191-.083.389-.083.595 0 1.315 1.066 2.381 2.381 2.381h20.16c.96 0 1.783-.572 2.159-1.391l.041-.083c4.157-8.968 13.231-15.192 23.766-15.192 14.465 0 26.19 11.726 26.19 26.19 0 14.465-11.726 26.191-26.19 26.191-10.535 0-19.609-6.225-23.766-15.191l-.041-.084c-.376-.82-1.199-1.393-2.16-1.393h-20.159c-1.315 0-2.381 1.066-2.381 2.381 0 .207.035.406.083.596 5.551 21.564 25.127 37.5 48.424 37.5 27.614 0 50-22.385 50-50 0-27.614-22.386-50-50-50z\">
                         <animateTransform attributeName=\"transform\" begin=\"0ms\" dur=\"600ms\" fill=\"freeze\" type=\"rotate\" repeatDur=\"indefinite\" values=\"0 50 50;359 50 50\" keyTimes=\"0;1\" calcMode=\"spline\" keySplines=\"0.42 0 0.58 1\"></animateTransform>
                       </path>
                       <circle fill=\"#fff\" cx=\"50\" cy=\"50\" r=\"11.905\"></circle>"})}])

(defn contact-us-inner [controls-ch]
  [:a {:on-click #(put! controls-ch [:intercom-dialog-raised])}
   " contact us "])

(defn messages [messages]
  [:div.row-fluid
   (when (pos? (count messages))
     [:div#build-messages.offset1.span10
      (map (fn [message]
             [:div.alert.alert-info
              [:strong "Warning: "]
              [:span {:dangerouslySetInnerHTML #js {"__html" (:message message)}}]])
           messages)])])

(def icon-shapes
  {:turn {:path "M50,0C26.703,0,7.127,15.936,1.576,37.5c-0.049,0.191-0.084,0.389-0.084,0.595c0,1.315,1.066,2.381,2.381,2.381h20.16c0.96,0,1.783-0.572,2.159-1.391c0,0,0.03-0.058,0.041-0.083C30.391,30.033,39.465,23.809,50,23.809c14.464,0,26.19,11.726,26.19,26.19c0,14.465-11.726,26.19-26.19,26.19c-10.535,0-19.609-6.225-23.767-15.192c-0.011-0.026-0.041-0.082-0.041-0.082c-0.376-0.82-1.199-1.392-2.16-1.392H3.874c-1.315,0-2.381,1.066-2.381,2.38c0,0.206,0.035,0.406,0.084,0.597C7.127,84.063,26.703,100,50,100c27.614,0,50-22.387,50-50C100,22.385,77.614,0,50,0z"}
   :circle {:path "" :cx "50" :cy "50" :r "11.904"}
   :pass {:path "M65.151,44.949L51.684,58.417l-3.367,3.367c-0.93,0.93-2.438,0.93-3.367,0l-3.368-3.367l-6.734-6.733 c-0.93-0.931-0.93-2.438,0-3.368l3.368-3.367c0.929-0.93,2.437-0.93,3.367,0L46.633,50l11.785-11.785 c0.931-0.929,2.438-0.929,3.367,0l3.366,3.367C66.082,42.511,66.082,44.019,65.151,44.949z"}
   :fail {:path "M61.785,55.051L56.734,50l5.051-5.05c0.93-0.93,0.93-2.438,0-3.368l-3.367-3.367c-0.93-0.929-2.438-0.929-3.367,0L50,43.265l-5.051-5.051c-0.93-0.929-2.437-0.929-3.367,0l-3.367,3.367c-0.93,0.93-0.93,2.438,0,3.368l5.05,5.05l-5.05,5.051c-0.93,0.929-0.93,2.438,0,3.366l3.367,3.367c0.93,0.93,2.438,0.93,3.367,0L50,56.734l5.05,5.05c0.93,0.93,2.438,0.93,3.367,0l3.367-3.367C62.715,57.488,62.715,55.979,61.785,55.051z"}
   :clock {:path "M59.524,47.619h-7.143V30.952c0-1.315-1.066-2.381-2.381-2.381c-1.315,0-2.381,1.065-2.381,2.381V50c0,1.315,1.066,2.38,2.381,2.38h9.524c1.314,0,2.381-1.065,2.381-2.38S60.839,47.619,59.524,47.619z"}})

(defn icon [{icon-type :type icon-name :name}]
  [:svg {:class (str "icon-" (name icon-name))
         :xmlns "http://www.w3.org/2000/svg"
         :viewBox "0 0 100 100"}
   (when (= :status icon-type)
     [:path {:class icon-name :fill "none" :d (get-in icon-shapes [:turn :path])}])
   [:path {:class icon-name :fill "none" :d (get-in icon-shapes [icon-name :path])}]])
