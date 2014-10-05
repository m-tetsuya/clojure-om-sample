(ns myapp.core
  (:import (java.net URL))
  (:require
     [liberator.core :refer [resource defresource]]
     [ring.middleware.params :refer [wrap-params]]
     [ring.adapter.jetty :refer [run-jetty]]      
     [compojure.core :refer [defroutes ANY GET ]]
     [compojure.route :as route]
     [clojure.java.io :as io]
     )
  
  (:use
     [ring.middleware.reload]
     [korma.core]
     [korma.db]
     )
  )

(defroutes app
           (GET "/" [] (slurp "resources/public/index.html"))
           (route/resources "/")
           (route/not-found "Not Found")
           )

(def handler 
  (-> app 
    (wrap-params)
    ))

