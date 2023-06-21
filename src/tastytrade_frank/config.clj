(ns tastytrade-frank.config
  (:require [clojure.edn :as edn]
            [clojure.core.memoize :as memo]))

(defn- config* []
  (let [fpath (System/getenv "CONFIG_TASTYTRADE_FRANK_FPATH")]
    (-> fpath slurp edn/read-string)))

(def ^:private config
  ;; in milliseconds so (* 1000 60) = 1 minute
  (memo/ttl config* :ttl/threshold (* 1000 60)))

(defn get-username []
  (get-in (config) [:clients :tastytrade :username]))

(defn get-password []
  (get-in (config) [:clients :tastytrade :password]))

(defn get-api-host []
  (get-in (config) [:clients :tastytrade :host]))


(comment

  (get-username)

  (config)


  )

(defn get-account-number []
  (get-in (config) [:clients :tastytrade :acct-num]))

(comment

  (get-account-number)


  )

(defn get-api-ninjas-api-key []
  (get-in (config) [:clients :api-ninjas :api-key]))

(comment

  (get-api-ninjas-api-key)
  
  )

(defn get-polygon-api-key []
  (get-in (config) [:clients :polygon :api-key]))

(comment

  (get-polygon-api-key)

  )
