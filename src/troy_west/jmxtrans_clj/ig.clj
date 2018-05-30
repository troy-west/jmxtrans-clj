(ns troy-west.jmxtrans-clj.ig
  (:require [troy-west.jmxtrans-clj :as jmxtrans-clj]
            [integrant.core :as ig])
  (:import (com.googlecode.jmxtrans JmxTransformer)))

(defmethod ig/init-key :jmxtrans/transformer
  [_ config]
  (let [transformer (-> config
                        jmxtrans-clj/jmxtrans-config
                        jmxtrans-clj/jmx-transformer)]
    (.start transformer)

    (.invoke (doto (.getDeclaredMethod JmxTransformer "unregisterMBeans" (into-array Class []))
               (.setAccessible true))
             transformer
             (into-array Object []))

    {:config      config
     :transformer transformer}))

(defmethod ig/halt-key! :jmxtrans/transformer
  [_ {:keys [transformer]}]
  (.stop transformer))
