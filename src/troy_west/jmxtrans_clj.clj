(ns troy-west.jmxtrans-clj
  (:require [clojure.java.io :as io])
  (:import (com.googlecode.jmxtrans JmxTransformer)
           (com.googlecode.jmxtrans.cli JmxTransConfiguration)
           (com.googlecode.jmxtrans.guice JmxTransModule)
           (com.googlecode.jmxtrans.classloader ClassLoaderEnricher)))

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
           use-separate-executors]}]
  (doto (JmxTransConfiguration.)
    (cond->
        continue-on-json-error (.setContinueOnJsonError continue-on-json-error)
        process-config-dir     (.setProcessConfigDir (io/file process-config-dir))
        process-config-file    (.setProcessConfigFile (io/file (io/resource process-config-file)))
        run-endlessly          (.setRunEndlessly run-endlessly)
        quartz-properties-file (.setQuartzPropertiesFile quartz-properties-file)
        run-period             (.setRunPeriod run-period)
        help                   (.setHelp help)
        additional-jars        (.setAdditionalJars additional-jars)
        query-processor-executor-pool-size
        (.setQueryProcessorExecutorPoolSize query-processor-executor-pool-size)
        query-processor-executor-work-queue-capacity
        (.setQueryProcessorExecutorWorkQueueCapacity query-processor-executor-work-queue-capacity)
        result-processor-executor-pool-size
        (.setResultProcessorExecutorPoolSize result-processor-executor-pool-size)
        result-processor-executor-work-queue-capacity
        (.setResultProcessorExecutorWorkQueueCapacity result-processor-executor-work-queue-capacity)
        use-separate-executors (.setUseSeparateExecutors use-separate-executors))))

(defn jmx-transformer
  [trans-config]
  (let [enricher (ClassLoaderEnricher.)]
    (doseq [jar (.getAdditionalJars trans-config)]
      (.add enricher jar))

    (-> trans-config
        JmxTransModule/createInjector
        (.getInstance JmxTransformer))))
