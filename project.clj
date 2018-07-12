(defproject com.troy-west/jmxtrans-clj "0.1.3-SNAPSHOT"
  :description "Clojure wrapper around jmxtrans"

  :url "https://github.com/troy-west/jmxtrans-clj"

  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v20.html"}

  :plugins [[lein-cljfmt "0.5.7" :exclusions [org.clojure/clojure]]
            [jonase/eastwood "0.2.5" :exclusions [org.clojure/clojure]]
            [lein-kibit "0.1.6" :exclusions [org.clojure/clojure org.clojure/tools.reader]]]

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.jmxtrans/jmxtrans-core "270" :exclusions [com.google.inject/guice]]
                 [com.google.inject/guice "4.1.0"]
                 [integrant "0.6.3"]
                 [cheshire "5.8.0"]]

  :deploy-repositories [["releases" {:url "https://clojars.org/repo/" :creds :gpg}]]

  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]

  :aliases {"smoke" ["do" ["clean"] ["check"] ["test"] ["kibit"] ["cljfmt" "check"] ["eastwood"]]})