(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:use hugo.db.sqlite)
  (:use clojure.contrib.command-line) 
  (:use hugo.db.createsqlite))

(def *base-url* "http://www.thehugoawards.org/hugo-history/")

(defn create-and-load-db
  "Creates a sqlite db and loads it with the parsed data"
  [links]
  (create-db)
  (let [awards-data (map #(hugo.parser/parse-best-novel-nominees (fetch-url (:href %))) links)]
   (hugo.db.createsqlite/process-awards awards-data)))

(defn -main [& args]
 (with-command-line args
   "Creates a sqlite3 database of all the Hugo Best Novel
    Award winners and nominees"
   [[drop "If true, will drop tables prior to creating the database" "false"] remaining]
    (if (= drop "true") (drop-tables))
    (let [urls (hugo.parser/get-award-links *base-url*)]
      (create-and-load-db (take 12 urls)))))
