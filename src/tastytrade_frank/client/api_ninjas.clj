(ns tastytrade-frank.client.api-ninjas
  (:require [clojure.string :as string]
            [clj-http.client :as http]
            [tastytrade-frank.config :as config]
            [tastytrade-frank.client.helper :as client-helper]))

(defn get-risk-free-rate []
  (let [uri "https://api.api-ninjas.com/v1/interestrate?name=libor"
        resp (-> (http/get uri
                        {:headers {"X-Api-Key" (config/get-api-ninjas-api-key)}})
                 client-helper/assoc-body-data)
        $ (->> (get-in resp [:body-data :non_central_bank_rates])
               (some (fn [x]
                       (let [name-lc (-> x :name string/lower-case)]
                         (when (= "usd libor - 1 month" name-lc)
                           x)))))]
    (:rate_pct $)))

(comment

  (get-risk-free-rate)
  (type (get-risk-free-rate))


  )
