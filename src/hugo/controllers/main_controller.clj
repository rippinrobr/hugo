(ns hugo.controllers.main-controller
  (:use hugo.views.main-view)
  (:use hugo.db.sqlite))

(defn- is-winner [data]
  (if (= 1 (:winner data)) "*" ""))

(defn- format-data [year]
   (concat [(str year " Hugo Award Nominees")]
          (map #(str "           " (is-winner %) (:title %) " by " (:author %)) (get-nominees year))
           ["      "]))

(defn- get-nominees-data-list []
  (concat (reduce concat (map #(format-data (:year %)) ( get-years))) ["* Indicates award winner"]))

(defn show-list []
  (create-home-view (get-nominees-data-list)))