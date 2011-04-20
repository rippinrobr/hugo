(ns cmdline
  (:gen-class)
  (:use hugo.parser))

(defn -main [& args]
  (println (map #(format "%s\n" (:award %)) (hugo.parser/get-awards-per-year "http://www.thehugoawards.org/hugo-history/2010-hugo-awards/"))))
