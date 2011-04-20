(ns cmdline
  (:gen-class)
  (:use hugo.parser)
  (:require [clojure.contrib.duck-streams :as duck]))

(def url "http://www.thehugoawards.org/hugo-history/2010-hugo-awards/")

(defn -main [& args]
  (let [novels (first (hugo.parser/get-awards-per-year url))]
    (duck/spit "2010_hugo_awards.txt" (format "%s%s\n" (:award novels) (hugo.parser/format-nominees (:books novels))))))
