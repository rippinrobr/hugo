(ns hugo.text-formatting
  (:use hugo.parser))

(defn if-work-not-nil 
  "Formats the book's line like so: title author and WINNER if it 
   won as long as work is not nil"
  [work]
  (str (if (not (nil? (:title work))) (str "\n\t" (:title work) " - " (:author work))) 
       (if (not (nil? (:winner work))) (str " (WINNER)") ) ""))

(defn format-nominees [works]
  "formats all winners and nominees for a given award into one string"
  (apply str (map if-work-not-nil works)))

(defn format-output [novels]
  "formats the award section including award title, winners and nominees"
  (format "%s - Best Novel%s\n" (:year novels) (format-nominees (:books novels))))
