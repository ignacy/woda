(ns woda.core
  (:import [com.gargoylesoftware.htmlunit
            BrowserVersion WebClient]))

(defonce browser (WebClient. (BrowserVersion/FIREFOX_3_6)))

(defn open [url]
  (.asText (.getPage browser url)))
