(ns hugo.parser
 (:require [net.cgrand.enlive-html :as html])
 (:require [clojure.contrib.string :as ccstring]))

(defstruct work :winner :title :author)

(defn fetch-url 
  "Retrieves the web page specified by the url"
  [url] (html/html-resource (java.net.URL. url)))

(defn get-book-info 
  "Formats the book data so that each book has a title which contais the book's title, author, and sometimes the publisher.  I also shows if
   the book was a winner"
  [nominees]
  (map #(if (not (nil? (first (:content (first (:content %)))))) 
                (struct work (if (not (nil? (:attrs %))) (:class (:attrs %))) 
                             (str (ccstring/replace-str "\"" "" (first (:content (first (:content %))))) " ") 
                             (ccstring/replace-str " by " "" (second (:content %)))))
                             nominees))

(defn parse-award-page 
  "Takes the page data retrieved and formats it in such away that each hugo award group is stored with ((award title) (winner and nominees))"
  [page-content]
  (partition 2 (interleave 
                         (last (split-at 3 (keep #(if (nil? (:attrs %)) (:content %)) 
                                              (html/select page-content #{[:div#content (html/but :p.tropy) :p] [:p html/first-child]}))))
  			 (map #(:content %) (html/select page-content #{[:div#content :ul ] })) )))

(defn get-awards-per-year 
  "Retrieves the awards page, parses out the categories, winners and nominees and then formats the data so that it can manipulated more easily."
  [url]
  (let [page-content (fetch-url url)]
    (map #(merge {:award (first (first %))} 
               {:books (get-book-info (rest (second %)))}
               {:year (first (:content (first (html/select page-content #{[:div#content :h2]}))))}) (parse-award-page page-content))))

(def *base-url* "http://www.thehugoawards.org/hugo-history/")

(defn get-award-links []
  (map #(:attrs %)   (html/select (fetch-url *base-url*)
                    #{[:div#content :li.page_item :a] [:li.page.item.subtext html/first-child]})))
