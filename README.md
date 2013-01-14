# woda

woda is a web application acceptance testing library written in clojure. It's inspired by [capybara](https://github.com/jnicklas/capybara) and [abrade](https://github.com/weavejester/abrade)

## Usage

I started writing woda as a separate testing framework, but I used [Midje](https://github.com/marick/Midje) to test it along the way.
What I noticed was that it was very easy to make Midje work as the correctens verification plumbing, and just focus on controling browser's behaviour. And this is the approach I have taken for now, so Midje is required for using woda to acceptance test your web application.

Here's a quick example of using woda with midje (taken from [woda's tests](https://github.com/ignacy/woda/blob/master/src/woda/core.clj)):

```lisp

   (facts "about selecting elements by css selectors"
        (-> (visit "http://localhost:8008")
            (get-element-by-css ".fun")
            (first)
            (content)) => "This is fun!"

         (-> (visit "http://localhost:8008")
            (get-element-by-css "NOT_EXISTING")) => nil)

```

## More detailed example

In my opinion there should be only a handful of acceptance tests for any of the common sizes of web applications.
Woda makes it easy to write this kind of tests, the idea is to optimize for the common case so you can test
most of the basic behaviour.

Example for a full test for authentication feature:

```lisp

(ns your_project_name.test.core
  (:use your_project.code)
  (:use woda.core)
  (:use midje.sweet))

(against-background
 [(before :facts (setup-server-visit-url-create-user-records) :after (do-cleanup))]

 (facts "authentication works when I enter correct user credentials"
        (-> (visit "my_url") ;; this probably could be placed in the before facts method
            (fill-in "login" "John")
            (fill-in "password" "secret")
            (click-button "Log in")
            (page-has? "You are logged in")) => truthy

         (-> (visit "mu_url")
             (fill-in "login" "John")
             (fill-in "password" "THIS_IS_NOT_JOHN_SECRET_PASSWORD")
             (click-button "Log in")
             (page-has? "You are logged in")) => falsey))

```

This kind tests are very similar to what you could write using capybara, you could probably extract
some common code out of those scenarios, although I wouldn't do that, if the scenario gets to long
to read easily - in most cases it means that it's just to long, and the user wouldn't feel comfortable
using it.

But since there are situations where common steps should be extracted, woda provides you with a `defstep` macro.
Here's an example:

```lisp

(ns your_project_name.test.core
  (:use your_project.code)
  (:use woda.core)
  (:use midje.sweet))

(defstep login
  (fill-in "login" "John")
  (fill-in "password" "Password")
  (click-button "Log in"))

(against-background
 [(before :facts (setup-server-visit-url-create-user-records) :after (do-cleanup))]

 (facts "authentication works when I enter correct user credentials"
        (-> (visit "htpp://homepage.example.com"
            (login)
            (page-has? "You are logged in")) => truthy)))

```

One important thing here is, that you need to define your tests before the tests.

## Docummentation

https://github.com/ignacy/woda/blob/master/src/woda/core.clj

## License

Copyright (C) 2013 Ignacy Moryc

Distributed under the Eclipse Public License, the same as Clojure.
