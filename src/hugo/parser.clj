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

(defn parse-year
  "takes the '2011 Hugo Awards' string and returns an int for the year"
  [yearstr]
    (Integer. (apply str (take 4 (apply str (:content (first yearstr)))))))

(defn create-work-struct
  [work-data]
  (if (not (nil? (first (:content (first (:content work-data)))))) 
    (struct work (if (not (nil? (:attrs work-data))) (:class (:attrs work-data))) 
      (ccstring/replace-str "\"" "" (ccstring/trim (first (:content (first (:content work-data))))))  
      (parse-author (second (:content work-data))))))
  
(defn get-book-info 
  [nominees] (map create-work-struct nominees))

(defn parse-best-novel-nominees
  [page-content]
    (struct category "Best Novel" 
      (keep #(if (not (nil? %)) %) 
                 (first (map #(get-book-info (:content %)) 
                         (html/select page-content [:div#content :ul])))) 
      (parse-year (html/select page-content #{[:div#content :h2]}))))
 
(defn get-award-links [url]
  (map :attrs (html/select (fetch-url url)
                    #{[:div#content :li.page_item :a] 
                      [:li.page.item.subtext html/first-child]})))
