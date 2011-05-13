(ns hugo.db.createsqlite
  (:use [clojure.contrib.sql 
      :only (with-connection with-query-results )] )
  (:use hugo.db.sqlite)
  (:require [clojure.contrib.sql :as sql]))

; this makes use db connection in the sqlite.clj file
(def new-db-conn (merge db {:create true}))

(defn drop-tables
  []
  (sql/with-connection new-db-conn
   (sql/drop-table :nominees)))

(defn create-tables
 "Creates the table needed to store the 
  winners and nominees."
 []
 (sql/create-table
   :nominees
   [:id :integer "PRIMARY KEY"]
   [:year :integer]
   [:title "varchar(64)"]
   [:author "varchar(32)"]
   [:winner "tinyint"]
   [:read_it "tinyint"]
   [:own_it "tinyint"]
   [:want_it "tinyint"]))

(defn create-db
 "Creates a new database"
 []
 (sql/with-connection new-db-conn
   (sql/transaction (create-tables))))

;-----------------------------------------
; process entries when loading from source
;-----------------------------------------
(defn add-new-nominees
  [noms]
  (let [year (:year noms)]
    (map #(add-nominee year %) (:books noms))))

(defn process-awards
  [awards-data] (map add-new-nominees awards-data))
