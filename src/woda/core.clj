(ns woda.core
  (:import [com.gargoylesoftware.htmlunit
            BrowserVersion WebClient]))

(def not-nil? (complement nil?))

"http://htmlunit.sourceforge.net/apidocs/com/gargoylesoftware/htmlunit/WebClient.html"
(defonce browser
  (WebClient. (BrowserVersion/FIREFOX_3_6)))

(defmacro defstep
  "This is a simple macro that allows users to create `steps` for reuse"
  [name & body]
  `(def ~name (fn [page#]
                (-> page#
                    ~@body))))

(defn visit
  "visit given *url* in the default browser"
  [url]
  (.getPage browser url))

(defn html-source
  "returns page source as HTML"
  [page]
  (.asXml page))

(defn content
  "get page content as text without markup"
  [page]
  (.asText page))

(defn page-has?
  "Check if page has a string content"
  [page string]
  (not-nil?
   (re-find (re-pattern string) (content page))))

(defn get-element-by-id
  "Get HtmlElement using it's id threows ElementNotFoundException if id is not found"
  [page id]
  (.getHtmlElementById page id))

(defn get-element-by-name
  "Find element by name attribute"
  [page name]
  (.getElementByName page name))

(defn get-element-by-css
  "Get element (list of matching elements) from page using CSS querya"
  [page css]
  (seq (.querySelectorAll page css)))

(defn execute-javascript
  "Executes passed JavaScript code in the context of the page"
  [page javascript]
  (.executeJavaScript page javascript)
  page)

(defn fill-in
  "Fill in *input-name* with given *value*"
  [page input-name value]
  (.setValueAttribute (get-element-by-name page input-name) value))

(defn click-button
  "Click on a button with *button-text*"
  [page button-text]
  (.click (.getInputByName (first (.getForms page)) button-text)))

(defn click-link
  "Click link with *text*"
  [page text]
  (.click (.getAnchorByText page text)))
