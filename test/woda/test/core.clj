(ns woda.test.core
  (:use woda.test.support)
  (:use [woda.core])
  (:use midje.sweet))

(against-background
 [(before :facts (setup-test-server) :after (stop-test-server))]
 (fact
  (content (visit "http://localhost:8008")) => "Hello\nThis is fun!"))
