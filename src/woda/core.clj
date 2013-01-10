(ns woda.core
  (:import [com.gargoylesoftware.htmlunit
            BrowserVersion WebClient]))

(defonce browser (WebClient. (BrowserVersion/FIREFOX_3_6)))

(defn visit [url]
  (.getPage browser url))

(defn content [page]
  (.asText page))
