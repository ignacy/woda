(defproject woda "0.0.1"
  :description "Browser acceptance testing for Clojure"
  :repositories {"stuart" "http://stuartsierra.com/maven2"}

  :dependencies [[org.clojure/clojure "1.3.0"]
                 [net.sourceforge.htmlunit/htmlunit "2.11"]]

  :dev-dependencies [[midje "1.4.0"]
                     [com.stuartsierra/lazytest "1.2.3"]])