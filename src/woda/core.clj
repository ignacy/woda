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

(defn html-source [page]
  "returns page source as HTML"
  (.asXml page))

(defn content [page]
  "get page content as text without markup"
  (.asText page))

(defn page-has? [page string]
  "Check if page has a string content"
  (not-nil?
   (re-find (re-pattern string) (content page))))

(defn get-element-by-id [page id]
  "Get HtmlElement using it's id threows ElementNotFoundException if id is not found"
  (.getHtmlElementById page id))

(defn get-element-by-name [page name]
  (.getElementByName page name))

(defn get-element-by-css [page css]
  "Get element (list of matching elements) from page using CSS querya"
  (seq (.querySelectorAll page css)))

(defn execute-javascript [page javascript]
  "Executes passed JavaScript code in the context of the page"
  (.executeJavaScript page javascript)
  page)

(defn fill-in [page input-name value]
  (.setValueAttribute (get-element-by-name page input-name) value))

(defn click-button [page button-text]
  (.click (.getInputByName (first (.getForms page)) button-text)))

(defn click-link [page text]
  (.click (.getAnchorByText page text)))
