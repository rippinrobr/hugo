(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:use hugo.text-formatting)
  (:use hugo.db.createsqlite)
  (:use clojure.contrib.command-line)
  (:require [clojure.contrib.duck-streams :as duck]))

(def *base-url* "http://www.thehugoawards.org/hugo-history/")

(defn prep-for-file 
  "Formats the output so it can be written correctly to the output file"
  [rec]
  (apply str (map #(str (format-output (first (hugo.parser/get-awards-per-year (:href %)))) "\n") rec)))

(defn process-args
  [flag]
  (if (= flag "text")
   (duck/spit "hugo_awards_best_novels.txt" 
        (prep-for-file (take 12 (hugo.parser/get-award-links *base-url*))))
   (println "simulated db creation")))

(defn -main [& args]
    "Runs the parser and then writes the results to the output file"
   (with-command-line args
     "Retrieve and print Best Novel nominees/winners or create a 
      sqlite db of all category nominees/winners"
      [[out "Indicates whether output should be to a file or DB" "text"]
       remaining] 
     
      (if (or (= out "db") (= out "text"))
        (process-args out)
        (println "valid options are db and text invalid out option: " out))))
