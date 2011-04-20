(ns hugo.parser
 (:require [net.cgrand.enlive-html :as html])
 (:require [clojure.contrib.duck-streams :as duck])
 (:require [clojure.contrib.string :as ccstring]))

(def *base-url* "http://www.thehugoawards.org/hugo-history/")

(defn fetch-url [url] (html/html-resource (java.net.URL. url)))

(defn get-book-info [nominees]
  (map #(if (not (nil? (first (:content (first (:content %)))))) (merge {:winner (if (not (nil? (:attrs %))) (:class (:attrs %)))} 
               {:title (str (ccstring/replace-str "\"" "" (first (:content (first (:content %))))) (second (:content %)))}
               )) nominees))

(defn parse-award-page [url]
  (partition 2 (interleave 
                         (last (split-at 3 (keep #(if (nil? (:attrs %)) (:content %)) 
                                              (html/select (fetch-url url) #{[:div#content (html/but :p.tropy) :p] [:p html/first-child]}))))
  			 (map #(:content %) (html/select (fetch-url url) #{[:div#content :ul ] }))) ))

(defn get-awards-per-year [url]
  (map #(merge {:award (first (first %))} 
               {:books (get-book-info (rest (second %)))}) (parse-award-page url)))

(defn if-work-not-nil [work]
  (str (if (not (nil? (:title work))) (str "\n\t" (:title work)) ) (if (not (nil? (:winner work))) (str " (WINNER)") ) ""))

(defn format-nominees [works]
  (apply str (map if-work-not-nil works)))

;--------------------------------------------------------------------------
; The code under he will come into play in the second post on this subject
;(defn get-award-links []
  ;(map #(:attrs %)   (html/select (fetch-url *base-url*)
                    ;#{[:div#content :li.page_item :a] [:li.page.item.subtext html/first-child]})))

;(defn get-awards []
;  (map #(merge % {:nominees (first (get-awards-per-year (:href %)))}) (get-award-links)))

;(defn format-output [awards]
  ;(map #(str "\n" (:award %) (format-nominees (:books %)) "\n") awards))

;(defn testaa [rec]
  ;(duck/spit "2010_hugo.txt" (apply str (format-output rec))))

