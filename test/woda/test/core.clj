(ns woda.test.core
  (:import [com.gargoylesoftware.htmlunit ElementNotFoundException])
  (:use woda.test.support)
  (:use [woda.core])
  (:use midje.sweet))

(against-background
 [(before :facts (setup-test-server) :after (stop-test-server))]

 (facts "about checking page content"
        (-> (visit "http://localhost:8008")
            (page-has? "Hello")) => truthy

        (-> (visit "http://localhost:8008")
            (page-has? "Unicorn")) => falsey)

 (facts "about checking page source"
        (-> (visit "http://localhost:8008")
            (html-source)) => ugly-html-source-of-page)

 (facts "about executing arbitrary JavaScript code on the page"
        (-> (visit "http://localhost:8008")
            (execute-javascript javascript-that-appends-a-text)
            (content)) => "Hello\nThis is a subtitlewoda is COOL\nThis is fun!\nFollow me! Submit")

 (facts "about selecting element by id"
        (-> (visit "http://localhost:8008")
            (page-get-element-by-id "subtitle")
            (content)) => "This is a subtitle"

        (-> (visit "http://localhost:8008")
            (page-get-element-by-id "NOT_EXISTING_ID")) => (throws ElementNotFoundException ))

 (facts "about clicking links"
        (-> (visit "http://localhost:8008")
            (click-link "Follow me!")
            (page-has? "I'm on a second page!")) => truthy)

 (facts "about submiting forms"
        (-> (visit "http://localhost:8008")
            (fill-in "name" "Ignacy")
            (fill-in "password" "secret")
            (click-button "Log in")
            (page-has? "You are logged in")) => truthy))
