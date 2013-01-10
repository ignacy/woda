(ns woda.test.support
  (:use lamina.core)
  (:use aleph.http))


(defn test-fixture [channel request]
  "This handler will return the page used in all other tests"
  (enqueue channel
           {:status 200
            :headers {"content-type" "text/html"}
            :body (slurp "fixtures/fixture.html") }))

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
