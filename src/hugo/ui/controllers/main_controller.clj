(ns hugo.ui.controllers.main-controller
  (:use hugo.text-formatting)
  (:use hugo.db.sqlite))

(defn get-nominees-data []
  (get-nominees))


