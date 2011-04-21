(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:use hugo.text-formatting)
  (:require [clojure.contrib.duck-streams :as duck]))

(defn -main [& args]
    "Runs the parser and then writes the results to the output file"
    (duck/spit "hugo_awards_best_novels.txt" 
               (prep-for-file (take 11 (hugo.parser/get-award-links)))))
