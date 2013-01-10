(ns woda.test.core
  (:use [woda.core])
  (:use midje.sweet)
  (:use lamina.core)
  (:use aleph.http))

(defonce test-server (atom nil))

(defn hello-world [channel request]
  (enqueue channel
           {:status 200
            :headers {"content-type" "text/html"}
            :body "Hello World!"}))

(defn setup-test-server []
  (if @test-server
    (println "Test server running..")
    (let [port 8008]
      (swap! test-server
             (fn [_] (start-http-server hello-world {:port port}))))))

(defn stop-test-server []
  (when @test-server
    (do
      (@test-server)
      (swap! test-server (fn [_] nil)))))


(against-background
 [ (before :facts (setup-test-server) :after (stop-test-server)) ]
 (fact
  (open "http://localhost:8008") => "Hello World!"))
