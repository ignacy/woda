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
  "Get HtmlElement using it's id threows ElementNotFoundException if id is not found"
  (.getHtmlElementById page id))

(defn page-get-element-by-name [page name]
  (.getElementByName page name))

(defn fill-in [page input-name value]
  (.setValueAttribute (page-get-element-by-name page input-name) value))

(defn click-button [page button-text]
  (.click (.getInputByName (first (.getForms page)) button-text)))

(defn click-link [page text]
  (.click (.getAnchorByText page text)))
