(ns woda.core
  (:import [com.gargoylesoftware.htmlunit
            BrowserVersion WebClient]))

(def not-nil? (complement nil?))

"http://htmlunit.sourceforge.net/apidocs/com/gargoylesoftware/htmlunit/WebClient.html"
(defonce browser
  (WebClient. (BrowserVersion/FIREFOX_3_6)))

(defn visit [url]
  "visit given *url* in the default browser"
  (.getPage browser url))

(defn content [page]
  "get page content as text without markup"
  (.asText page))

(defn page-has? [page string]
  "Check if page has a string content"
  (not-nil?
   (re-find (re-pattern string) (content page))))

(defn page-get-element-by-id [page id]
  "Get HtmlElement using it's id"
  (.getHtmlElementById page id))

(defn click-link [page text]
  (.click (.getAnchorByText page text)))
