(ns myapp.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
     [om.dom :as dom :include-macros true]
     [cljs.core.async :refer [put! chan <!]]
     ))

(enable-console-print!)

(def app-state (atom {:text "Hello world! 222"
                      :list ["lion" "zebra" "buffalo"]
                      :contacts
                      [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
                       {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
                       {:first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
                       {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
                       {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
                       {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]
                      }))

(om/root
  (fn [app owner]
    (reify om/IRender
      (render [_]
        (dom/h1 #js {:style #js {:color "red"}} (:text app)))))
  app-state
  {:target (. js/document (getElementById "app0"))})

(om/root
  (fn [app owner]
    (om/component
      (apply dom/ul #js {:className "animals"}
             (map (fn [text] (dom/li nil text)) (:list app)))))
  app-state
  {:target (. js/document (getElementById "app1"))})

(swap! app-state assoc :text "Do it live!")

(defn middle-name [{:keys [middle middle-initial]}]
  (cond
    middle ( str " " middle)
    middle-initial (str " " middle-initial ".")))

(defn display-name [{:keys [first last] :as contact}]
  (str last " , " first (middle-name contact)))

(defn contact-view [contact owner]
  (reify
    om/IRenderState
    (render-state [this {:keys [delete]}]
            (dom/li nil 
                    (dom/span nil (display-name contact))
                    (dom/button 
                      #js {:onClick (fn [e] (put! delete @contact))} "delete")))))

(defn contacts-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
                {:delete (chan)})
    om/IWillMount
    (will-mount [_]
                (let [delete (om/get-state owner :delete)]
                  (go (loop []
                        (let [contact (<! delete)]
                          (om/transact! app :contacts
                                        (fn [xs] (vec ( remove #(= contact %) xs ))))
                          (recur))))))
    om/IRenderState
    (render-state [this {:keys [delete]}]
                  (dom/div nil
                           (dom/h2 nil "Contact list")
                           (apply dom/ul nil
                                  (om/build-all contact-view (:contacts app)
                                                {:init-state {:delete delete}}))))))


(om/root contacts-view app-state
  {:target (. js/document (getElementById "contacts"))})


