(defproject com.troy-west/jmxtrans-clj "0.1.1"
  :description "Clojure wrapper around jmxtrans"

  :url "https://github.com/troy-west/jmxtrans-clj"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.jmxtrans/jmxtrans "270"]
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
                  ["vcs" "push"]])
