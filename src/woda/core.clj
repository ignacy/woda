(ns woda.core
  (:import [com.gargoylesoftware.htmlunit
            BrowserVersion WebClient]))

(defn bbc-top-stories []
  (map text
     (-> (browser :firefox)
         (open "http://bbc.co.uk/news")
         (css "a.story"))))
