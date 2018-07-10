(ns troy-west.jmxtrans
  (:require [clojure.java.io :as io]
            [cheshire.core :as json]
            [integrant.core :as ig])
  (:import (java.io File)
           (com.googlecode.jmxtrans JmxTransformer)
           (com.googlecode.jmxtrans.cli JmxTransConfiguration)
           (com.googlecode.jmxtrans.guice JmxTransModule)
           (com.googlecode.jmxtrans.classloader ClassLoaderEnricher)))

(defn gen-temp-config
  [queries]
  (let [temp-dir    (io/file (System/getProperty "java.io.tmpdir"))
        config-file (File/createTempFile "jmxtrans-config" ".json" temp-dir)]
    (spit config-file (json/encode queries {:pretty true}))
    config-file))

(defn jmxtrans-config
  [{:keys [continue-on-json-error
           process-config-dir
           process-config-file
           run-endlessly
           quartz-properties-file
           run-period
           help
           additional-jars
           query-processor-executor-pool-size
           query-processor-executor-work-queue-capacity
           result-processor-executor-pool-size
           result-processor-executor-work-queue-capacity
           use-separate-executors
           queries]}]
  (let [process-config-file (if queries
                              (or process-config-file (gen-temp-config queries))
                              process-config-file)]
    (cond-> (JmxTransConfiguration.)
      continue-on-json-error
      (doto (.setContinueOnJsonError continue-on-json-error))
      process-config-dir
      (doto (.setProcessConfigDir (io/file process-config-dir)))
      process-config-file
      (doto (.setProcessConfigFile (if (instance? File process-config-file)
                                     process-config-file
                                     (io/file (io/resource process-config-file)))))
      run-endlessly
      (doto (.setRunEndlessly run-endlessly))
      quartz-properties-file
      (doto (.setQuartzPropertiesFile quartz-properties-file))
      run-period
      (doto (.setRunPeriod run-period))
      help
      (doto (.setHelp help))
      additional-jars
      (doto (.setAdditionalJars additional-jars))
      query-processor-executor-pool-size
      (doto (.setQueryProcessorExecutorPoolSize query-processor-executor-pool-size))
      query-processor-executor-work-queue-capacity
      (doto (.setQueryProcessorExecutorWorkQueueCapacity query-processor-executor-work-queue-capacity))
      result-processor-executor-pool-size
      (doto (.setResultProcessorExecutorPoolSize result-processor-executor-pool-size))
      result-processor-executor-work-queue-capacity
      (doto (.setResultProcessorExecutorWorkQueueCapacity result-processor-executor-work-queue-capacity))
      use-separate-executors
      (doto (.setUseSeparateExecutors use-separate-executors)))))

(defn jmx-transformer
  [trans-config]
  (let [enricher (ClassLoaderEnricher.)]
    (doseq [jar (.getAdditionalJars ^JmxTransConfiguration trans-config)]
      (.add enricher ^File jar)))
  (.getInstance (JmxTransModule/createInjector trans-config) ^Class JmxTransformer))

(defmethod ig/init-key :jmxtrans/transformer
  [_ config]
  (let [transformer (jmx-transformer (jmxtrans-config config))]
    (.start ^JmxTransformer transformer)
    {:config      config
     :transformer transformer}))

(defmethod ig/halt-key! :jmxtrans/transformer
  [_ {:keys [transformer]}]
  (.invoke (doto (.getDeclaredMethod JmxTransformer "unregisterMBeans" (into-array Class []))
             (.setAccessible true))
           transformer
           (into-array Object []))
  (.stop ^JmxTransformer transformer))