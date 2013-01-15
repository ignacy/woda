# woda

woda is a web application acceptance testing library written in clojure. It's inspired by [capybara](https://github.com/jnicklas/capybara) and [abrade](https://github.com/weavejester/abrade)

    [org.clojars.ignacy/woda "0.1.1"]

## Usage

I started writing woda as a separate testing framework, but I used [Midje](https://github.com/marick/Midje) to test it along the way.
What I noticed was that it was very easy to make Midje work as the correctens verification plumbing, and just focus on controling browser's behaviour. And this is the approach I have taken for now, so Midje is required for using woda to acceptance test your web application.

Here's a quick example of using woda with midje (taken from [woda's tests](https://github.com/ignacy/woda/blob/master/test/woda/test/core.clj)):

```clojure

(facts "about selecting elements by css selectors"
     (-> (visit "http://localhost:8008")
         (get-element-by-css ".fun")
         (first)
         (content)) => "This is fun!"

     (-> (visit "http://localhost:8008")
         (get-element-by-css "NOT_EXISTING")) => nil)

```

## More detailed example

Woda makes it easy to write a handful of acceptance test, using most common interaction methods.
Example of a test for authentication feature:

```clojure

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

This kind of tests are very similar to what you could write using capybara. You could probably extract
some common code out of those scenarios, although I wouldn't do that. If the scenario gets to long
to read easily - in most cases it means that it's just to long, and the user wouldn't feel comfortable
using it. And some duplication in tests like that is good for documentation.

But since there are situations where common steps should be extracted, woda provides a `defstep` macro.
Here's an example:

```clojure

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


## Running test

Since woda is using Midje, and really at this point is just a set of helper functions for Midje,
all test can be run using [lein midje](https://github.com/marick/lein-midje)

Because it uses HTMLUnit to simulate the browser, the startup time is slow, but you can avoid it completely
by using Midje's autotest feature. If you run:

    lein midje --lazytest

You will basically have to wait for the framework to start only the first time. After that the tests will run
on each change to the source/test code, and they will be very fast!

## Docs

Available methods:

- (visit url) - basic navigation, visits url in the default browser, returns a page. Most tests start with this call, and use threading macro
to pass the resulting page implicitly through other functions.

- (get-element-by-id _page_ id) - given page and id searches for html element with that ID on the page
- (get-element-by-name _page_ name) - matches elements by name attribute
- (get-element-by-css _page_ css) - use css query to find element on the page

- (execute-javascript _page_ javascript) - executes any javascript in page context

- (click-button _page_ button-text)
- (click-link _page_ link-text)
- (fill-in _page_ input-name value)

- (page-has? _page_ string)
- (content _page_) - returns page content as string (without markup)
- (html-source _page_) - returns page source (with markup)


## License

Copyright (C) 2013 Ignacy Moryc

Distributed under the Eclipse Public License, the same as Clojure.
