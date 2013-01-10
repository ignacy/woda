(ns woda.test.core
  (:use woda.test.support)
  (:use [woda.core])
  (:use midje.sweet))

(against-background
 [(before :facts (setup-test-server) :after (stop-test-server))]
 (fact
  (-> (visit "http://localhost:8008")
      (page-has? "Hello")) => truthy)
 (fact
  (-> (visit "http://localhost:8008")
      (page-has? "Unicorn")) => falsey)
 (fact
  (-> (visit "http://localhost:8008")
      (page-get-element-by-id "subtitle")
      (content)) => "This is a subtitle")

 (fact
  (-> (visit "http://localhost:8008")
      (click-link "Follow me!")
      (page-has? "I'm on a second page!")) => truthy)

 )
