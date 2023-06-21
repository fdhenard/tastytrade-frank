(ns tastytrade-frank.tastytrade-frank
  "FIXME: my new org.corfield.new/scratch project."
  (:require [options-formulae-clj.core :as options-formulae]
            [tastytrade-frank.client.polygon :as polygon]
            [tastytrade-frank.client.tastytrade :as tastytrade]
            [tastytrade-frank.client.api-ninjas :as api-ninjas]))

;; (defn exec
;;   "Invoke me with clojure -X tastytrade-frank.tastytrade-frank/exec"
;;   [opts]
;;   (println "exec with" opts))

;; (defn -main
;;   "Invoke me with clojure -M -m tastytrade-frank.tastytrade-frank"
;;   [& args]
;;   (println "-main with" args))


(comment

  (tastytrade/get-option-chains "AMZN")


  )

(defn option->volatility [{:keys [option-type
                                  days-to-expiration
                                  underlying-symbol]
                           strike-price-str :strike-price
                           #_#_:as option}]
  (let [
        call-or-put (case option-type
                      "C" :call
                      "P" :put
                      (throw (ex-info "invalid :option-type" {:option-type
                                                              option-type})))
        time-to-expiry-in-yrs (/ days-to-expiration 365.25)
        strike-price (Double/parseDouble strike-price-str)
        risk-free-rate (api-ninjas/get-risk-free-rate)
        target-value 0.1 ;; ?????????
        underlying-price (polygon/get-yesterday-close-price underlying-symbol)
        volatility (options-formulae/find-volatility
                    target-value
                    call-or-put
                    underlying-price
                    strike-price
                    time-to-expiry-in-yrs
                    risk-free-rate)]
    volatility))

(comment

  (def an-option
    (let [$ (-> (tastytrade/get-option-chains "AMZN")
                (get-in [:body-data :data :items])
                first)]
      $))
  an-option

  (tastytrade/get-equity "AMZN")
  (tastytrade/symbol-search "AMZN")

  )
