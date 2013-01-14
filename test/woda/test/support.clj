(ns woda.test.support
  (:use lamina.core)
  (:use aleph.http))

(def ugly-html-source-of-page "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html>\n  <head/>\n  <body>\n    <h1>\n      Hello\n    </h1>\n    <p id=\"subtitle\">\n      This is a subtitle\n    </p>\n    <p>\n      This is fun!\n    </p>\n    <a href=\"/second.html\">\n      Follow me!\n    </a>\n    <form method=\"GET\" id=\"onlyform\" action=\"/second.html\">\n      <input type=\"text\" id=\"name\" name=\"name\" value=\"\"/>\n      <input type=\"text\" id=\"password\" name=\"password\" value=\"\"/>\n      <input type=\"submit\" value=\"Submit\" name=\"Log in\"/>\n    </form>\n  </body>\n</html>\n")

(def javascript-that-appends-a-text "var node = document.createTextNode('woda is COOL'); document.getElementById('subtitle').appendChild(node);")

(defn test-fixture [channel request]
  "This handler will return the page used in all other tests"
  (let [uri (str (request :uri))
        home (slurp "fixtures/fixture.html")
        second (slurp "fixtures/second.html")
        response-body (if (= uri "/second.html") second home)]
    (enqueue channel
             {:status 200
              :headers {"content-type" "text/html"}
              :body  response-body})))

(defonce test-server (atom nil))

;; Setup and stop methods are based on this discussion
;; http://stackoverflow.com/questions/7602753/workflow-for-restarting-a-http-server-from-clojure-repl
(defn setup-test-server []
  (if @test-server
    (println "Test server running..")
    (let [port 8008]
      (swap! test-server
             (fn [_] (start-http-server test-fixture {:port port}))))))

(defn stop-test-server []
  (when @test-server
    (do
      (@test-server)
      (swap! test-server (fn [_] nil)))))
