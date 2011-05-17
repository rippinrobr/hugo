(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:use hugo.db.sqlite)
  (:use clojure.contrib.command-line) 
  (:use hugo.db.createsqlite))

(def *base-url* "http://www.thehugoawards.org/hugo-history/")

(defn get-data
  "Parses out the web page's data on the Best Novel nominees
   and winners"
  [url] 
    (hugo.parser/parse-best-novel-nominees (fetch-url url)))
  
(defn create-and-load-db
  "Creates a sqlite db and loads it with the parsed data"
  [links]
  (let [awards-data (map #(get-data (:href %)) links)]
   (create-db)
   (hugo.db.createsqlite/process-awards awards-data)))

(defn -main [& args]
"Creates a sqlite DB to store the Best Novel nominees and winners.
  It has a command line argument 'drop' that if set to true will drop
  the nominees table."
 (with-command-line args
   "Creates a sqlite3 database of all the Hugo Best Novel
    Award winners and nominees"
   [[drop "If true, will drop tables prior 
            to creating the database" "false"] remaining]
    (if (= drop "true") (drop-tables))
    (let [urls (hugo.parser/get-award-links *base-url*)]
      (create-and-load-db (take 12 urls)))))
