(ns esigrate.rest
  (:use org.httpkit.server
        esigrate.status)
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.handler :as handler]
            [esigrate.env :as env]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))



(defroutes migration-routes

           (GET ["/"] req
                (with-channel req channel
                              (send! channel {:status 200 :headers {"Content-Type" "text/plain"} :body (json/write-str {:status "esigrate is up"})})))

           (POST ["/migration"] req
                 ;start new migration with the specified dsl
                 ;returns a unique id to query the status
                 (with-channel req channel
                               (let [body (String. (.bytes (:body req)))
                                     parsed (json/read-str body :key-fn keyword)]
                                 (put-dsl-to-queue! parsed)
                                 (send! channel {:status 200 :headers {"Content-Type" "text/plain"} :body {:ok "migration started"}}))))
           (GET ["/migration/:id"] req
                ;return the status about the migration with the specified id
                (with-channel req channel
                              (let [id (:id (:params req))
                                    response {:ok "migration ongoing"}]
                                (send! channel {:status 200 :headers {"Content-Type" "text/plain"} :body {:response (get-status id)}}))))
           )


(def app
  (->
    (routes migration-routes)
    handler/api))

(defn init-rest-server! []
  (let [port (:rest-port env/props)
        opts {:port port}]
    (run-server app opts)
    (log/infof "Rest server started at port : %s" port)))

