(ns woda.core
  (:import [com.gargoylesoftware.htmlunit
            BrowserVersion WebClient]))

(def not-nil? (complement nil?))

(defonce browser (WebClient. (BrowserVersion/FIREFOX_3_6)))

(defn visit [url]
  (.getPage browser url))

(defn content [page]
  (.asText page))

(defn page-has? [page string]
  (not-nil?
   (re-find (re-pattern string) (content page))))
