(ns tastytrade-frank.client.helper
  (:require [cheshire.core :as json]))

(defn assoc-body-data [resp]
  (assoc resp :body-data (json/parse-string (:body resp) true)))
