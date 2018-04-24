(ns troy-west.jmxtrans-clj.ig
  (:require [troy-west.jmxtrans-clj :as jmxtrans-clj]
            [integrant.core :as ig]))

(defmethod ig/init-key :jmxtrans/transformer
  [_ config]
  (let [transformer (-> config
                        jmxtrans-clj/jmxtrans-config
                        jmxtrans-clj/jmx-transformer)]
    (.start transformer)
    {:config      config
     :transformer transformer}))

(defmethod ig/halt-key! :jmxtrans/transformer
  [_ {:keys [transformer]}]
  (.stop transformer))
