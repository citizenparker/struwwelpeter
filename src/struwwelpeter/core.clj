(ns struwwelpeter.core
  (:require [struwwelpeter.views :as views]
            [compojure.core :as compojure]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.force-reload :as force-reload]
            [ring.adapter.jetty :as jetty]))

(compojure/defroutes all-routes
                     (compojure/GET "/" [] (views/index))
                     (compojure/GET "/:id" [id] views/story)
                     (route/files "/")
                     (route/not-found "<h1>Page not found!</h1>"))

(def app
  (-> all-routes
    (force-reload/wrap-force-reload ['struwwelpeter.views])
    (handler/site)))

(defn start [port]
  (defonce server (jetty/run-jetty (var app) {:port port :join? false})))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") 8080))]
    (start port)))
