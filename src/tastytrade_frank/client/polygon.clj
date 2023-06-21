(ns tastytrade-frank.client.polygon
  (:require [clj-http.client :as http]
            [tastytrade-frank.config :as config]
            [tastytrade-frank.client.helper :as client-helper])
  (:import [java.time LocalDate]))

(comment

  (-> (LocalDate/now)
      (.minusDays 1)
      str)

  )

(defn get-yesterday-close-price [symbol]
  (let [yesterday-str (-> (LocalDate/now)
                          (.minusDays 1)
                          str)
        uri (str "https://api.polygon.io/v1/open-close/"
                 symbol "/"
                 yesterday-str
                 "?adjusted=true")
        resp (-> (http/get uri
                           {:headers
                            {"Authorization"
                             (str "Bearer " (config/get-polygon-api-key))}})
                 client-helper/assoc-body-data)]
    (get-in resp [:body-data :close])))

(comment

  (get-yesterday-close-price "AMZN")
  (type (get-yesterday-close-price "AMZN"))
  (config/get-polygon-api-key)

  )
