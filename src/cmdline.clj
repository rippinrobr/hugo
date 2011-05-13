(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:use hugo.db.sqlite)
  (:use hugo.db.createsqlite))

(def *base-url* "http://www.thehugoawards.org/hugo-history/")

(defn create-and-load-db
  "Creates a sqlite db and loads it with the parsed data"
  [links]
  (println "Number of links: " (count links))
  (create-db)
  (add-org "Hugo")
  (let [org-id (get-org-id "Hugo")
       awards-data (map #(hugo.parser/parse-best-novel-nominees (fetch-url (:href %))) links)]
       (hugo.db.createsqlite/process-awards awards-data)))

(defn -main [& args]
  (let [urls (hugo.parser/get-award-links *base-url*)]
    (create-and-load-db (take 2 urls))))
