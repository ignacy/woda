(defproject org.clojars.ignacy/woda "0.1.3"
  :description "Browser acceptance testing for Clojure"
  :url "https://github.com/ignacy/woda"
  :repositories {"stuart" "http://stuartsierra.com/maven2"}

  :dependencies [[org.clojure/clojure "1.4.0"]
                 [net.sourceforge.htmlunit/htmlunit "2.11"]
                 [midje "1.5.0"]
                 [aleph "0.3.0-beta7"]]

  :license {:name "Eclipse Public License"
            :distribution :repo}

  :profiles {:dev {:dependencies [[midje "1.5.0"]]}})
