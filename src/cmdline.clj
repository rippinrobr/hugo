(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:use hugo.text-formatting))

(def *base-url* "http://www.thehugoawards.org/hugo-history/")

(defn prep-for-file 
  "Formats the output so it can be written correctly to the output file"
  [rec]
  (apply str (map #(str (format-output (first (hugo.parser/get-awards-per-year (:href %)))) "\n") rec)))

(defn -main [& args]
    "Runs the parser and then writes the results to the output file"
    (spit "hugo_awards_best_novels.txt" 
               (prep-for-file (take 12 (hugo.parser/get-award-links *base-url*)))))
