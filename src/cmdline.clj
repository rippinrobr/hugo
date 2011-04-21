(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:use hugo.text-formatting)
  (:require [clojure.contrib.duck-streams :as duck]))

(defn prep-for-file 
  "Formats the output so it can be written correctly to the output file"
  [rec]
  (apply str (map #(str (format-output (first (hugo.parser/get-awards-per-year (:href %)))) "\n") rec)))

(defn -main [& args]
    "Runs the parser and then writes the results to the output file"
    (duck/spit "hugo_awards_best_novels.txt" 
               (prep-for-file (take 11 (hugo.parser/get-award-links)))))
