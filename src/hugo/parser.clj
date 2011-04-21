(ns hugo.parser
 (:require [net.cgrand.enlive-html :as html])
 (:require [clojure.contrib.string :as ccstring]))

(defn fetch-url 
  "Retrieves the web page specified by the url"
  [url] (html/html-resource (java.net.URL. url)))

(defn get-book-info 
  "Formats the book data so that each book has a title which contais the book's title, author, and sometimes the publisher.  I also shows if
   the book was a winner"
  [nominees]
  (map #(if (not (nil? (first (:content (first (:content %)))))) (merge {:winner (if (not (nil? (:attrs %))) (:class (:attrs %)))} 
               {:title (str (ccstring/replace-str "\"" "" (first (:content (first (:content %))))) (second (:content %)))}
               )) nominees))

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

;(defn if-work-not-nil 
  ;"Formats the book's line like so: title author and WINNER if it won as long as work is not nil"
  ;[work]
  ;(str (if (not (nil? (:title work))) (str "\n\t" (:title work)) ) (if (not (nil? (:winner work))) (str " (WINNER)") ) ""))

;(defn format-nominees [works]
  ;"formats all winners and nominees for a given award into one string"
  ;(apply str (map if-work-not-nil works)))

;(defn format-output [novels]
  ;"formats the award section including award title, winners and nominees"
  ;(format "%s - Best Novel%s\n" (:year novels) (format-nominees (:books novels))))
