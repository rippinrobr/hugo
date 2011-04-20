(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:require [clojure.contrib.duck-streams :as duck]))

(def url "http://www.thehugoawards.org/hugo-history/2010-hugo-awards/")

(defn -main [& args]
    (print (str "Retrieving " url "..."))
    (duck/spit "2010_hugo_awards.txt" 
        (hugo.parser/format-output (first (hugo.parser/get-awards-per-year url))))
    (println "Done!"))
