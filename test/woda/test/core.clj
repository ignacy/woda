(ns woda.test.core
  (:use [woda.core])
  (:use midje.sweet))

(fact
 (bbc-top-stories) => 10)
