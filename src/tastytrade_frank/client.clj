(ns tastytrade-frank.client
  (:require [clojure.tools.logging :as log]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [tastytrade-frank.config :as config]))


(defn- assoc-body-data [resp]
  (assoc resp :body-data (json/parse-string (:body resp) true)))

(defn- create-session []
  (let [payload {:login (config/get-username)
                 :password (config/get-password)
                 :remember-me true}
        resp (-> (http/post (str (config/get-api-host) "/sessions")
                              {:form-params payload})
                 assoc-body-data)]
    resp))



(comment


  (create-session)


  )

(def session (atom nil))

(defn- fresh-session []
  (let [sess-dereffed @session]
   (if sess-dereffed
     sess-dereffed
     (let [sess-data (get-in (create-session) [:body-data :data])
           _ (log/debug "session was falsey, refreshing")]
       (reset! session sess-data)
       sess-data))))

(defn- get-session-token []
  (get (fresh-session) :session-token))

(defn- tasty [http-fn uri]
  (let [resp (-> (http-fn (str (config/get-api-host) uri)
                          {:headers {"Authorization" (get-session-token)}})
                 assoc-body-data)]
    resp))

(defn get-positions []
  (tasty http/get (str "/accounts/" (config/get-account-number) "/positions")))

(comment

  (get-positions)

  )

(defn get-watchlists []
  (tasty http/get "/public-watchlists"))

(comment

  (get-watchlists)

  (let [resp (get-watchlists)
        $ (get-in resp [:body-data :data :items])]
    (map :name $))

  )

(defn get-watchlist [name]
  (tasty http/get (str "/public-watchlists/" name)))

(comment

  (get-watchlist "tasty IVR")

  (log/debug "hi")
  (log/info "from info")

  (get-watchlist "Consumer Discretionary")

  ;; ;; sectors
  ;; Basic Materials
  ;; Communication Services
  ;; Consumer Defensive
  ;; Consumer Discretionary
  ;; Energy
  ;; Financial Services
  ;; Healthcare
  ;; Industrials
  ;; Real Estate
  ;; Technology
  ;; Utilities

  )

(defn get-equity [symbol]
  (tasty http/get (str "/instruments/equities/" symbol))
  )

(comment

  (get-equity "AMZN")

  )

(defn get-option-chains [symbol]
  (tasty http/get (str "/option-chains/" symbol)))

(comment

  (get-option-chains "AMZN")

  )

(defn get-equity-option [symbol]
  (tasty http/get (str "/instruments/equity-options/" symbol)))

(comment

  (get-equity-option "AMZN  230609C00055000")

  )

(defn symbol-search [symbol]
  (tasty http/get (str "/symbols/search/" symbol)))

(comment

  (symbol-search "AMZN")

  )
