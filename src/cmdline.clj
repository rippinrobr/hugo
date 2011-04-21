(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:require [clojure.contrib.duck-streams :as duck]))

(defn prep-for-file [rec]
  (apply str (map #(str "Year " (format-output (first (hugo.parser/get-awards-per-year (:href %)))) "\n") rec)))

(defn -main [& args]
    (duck/spit "hugo_awards_best_novels.txt" (prep-for-file (take 10 (get-award-links)))))
