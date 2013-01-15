(ns woda.test.core
  (:import [com.gargoylesoftware.htmlunit ElementNotFoundException])
  (:use woda.test.support)
  (:use [woda.core])
  (:use midje.sweet))

(defstep login
  (fill-in "name" "Ignacy")
  (fill-in "password" "secret")
  (get-form-by-name "onlyform")
  (click-button "Log in"))

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


 (facts "about selecting element by id"
        (-> (visit "http://localhost:8008")
            (get-element-by-id "subtitle")
            (content)) => "This is a subtitle"

        (-> (visit "http://localhost:8008")
            (get-element-by-id "NOT_EXISTING_ID")) => (throws ElementNotFoundException ))

 (facts "about selecting elements by css selectors"
        (-> (visit "http://localhost:8008")
            (get-element-by-css ".fun")
            (first)
            (content)) => "This is fun!"

         (-> (visit "http://localhost:8008")
            (get-element-by-css "NOT_EXISTING")) => nil)

 (facts "about clicking links"
        (-> (visit "http://localhost:8008")
            (click-link "Follow me!")
            (page-has? "I'm on a second page!")) => truthy

        (-> (visit "http://localhost:8008")
            (click-link "NOTEXISTING")) => (throws ElementNotFoundException))

 (facts "about submiting forms"
        (-> (visit "http://localhost:8008")
            (login)
            (page-has? "You are logged in")) => truthy

        (-> (visit "http://localhost:8008")
            (get-form-by-name "onlyform")
            (click-button "THERE_IS_NO_SUCH_BUTTON")) => (throws ElementNotFoundException))

 (facts "about executing arbitrary JavaScript code on the page"
        (-> (visit "http://localhost:8008")
            (execute-javascript javascript-that-appends-a-text)
            (content)) => "Hello\nThis is a subtitlewoda is COOL\nThis is fun!\nFollow me! Submit"

        (-> (visit "http://localhost:8008")
            (execute-javascript "2/0 == 'this is wrong'")
            (get-element-by-id "subtitle") ;; let's check if it broke the page
            (content)) => "This is a subtitle"))
