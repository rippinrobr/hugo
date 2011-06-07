(ns cmdline
  (:gen-class)
  (:use hugo.ui.controllers.main-controller))

(defn -main [& args]
"Displays a list of all the nominees and winners since 2000 using Swing."
  (show-list))