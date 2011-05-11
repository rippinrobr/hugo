(ns hugo.parser
 (:require [net.cgrand.enlive-html :as html])
 (:require [clojure.string])
 (:require [clojure.contrib.string :as ccstring]))

(defstruct work :winner :title :author)
(defstruct category :award :books :year)

(defn fetch-url 
  "Retrieves the web page specified by the url and 
   makes an html-resource out of it which is used 
   by enlive."
  [url] (html/html-resource (java.net.URL. url)))

(defn split-author-publisher-str
  [authpubstr]
    (clojure.string/split (ccstring/replace-re #"^," "" (ccstring/replace-str "by " "" (ccstring/replace-str " by " "" authpubstr))) #"\[|\(" ))

(defn parse-author
  "Grabs the author's name"
  [authstr] (ccstring/trim (first (split-author-publisher-str authstr))))

(defn create-work-struct
  [work-data]
  (if (not (nil? (first (:content (first (:content work-data)))))) 
    (struct work (if (not (nil? (:attrs work-data))) (:class (:attrs work-data))) 
      (ccstring/replace-str "\"" "" (ccstring/trim (first (:content (first (:content work-data))))))  
      (parse-author (second (:content work-data))))))
  
(defn get-book-info 
  "Formats the book data so that each book has a title which contais 
   the book's title, author, and sometimes the publisher.  I also shows if
   the book was a winner"
  [nominees]
  (map create-work-struct nominees))

(defn parse-award-page 
  "Takes the page data retrieved and formats it in such away that each 
   hugo award group is stored with ((award title) (winner and nominees))"
  [page-content]
  (partition 2 
     (interleave 
       (split-at 4 
         (html/select page-content #{[:div#content :p] [:p html/first-child]})) 
       (map :content (html/select page-content #{[:div#content :ul ] })))))

(defn get-awards-per-year 
  "Retrieves the awards page, parses out the categories, 
   winners and nominees and then formats the data so 
   that it can manipulated more easily."
  [url]
  (let [page-content (fetch-url url) year (apply str (:content 
                  (first (html/select page-content #{[:div#content :h2]}))))]
    (map #(struct category (apply str (first %)) 
                           (get-book-info (rest (second %))) 
                           year)
         (parse-award-page page-content))))

(defn get-award-links [url]
  (map :attrs (html/select (fetch-url url)
                    #{[:div#content :li.page_item :a] 
                      [:li.page.item.subtext html/first-child]})))
