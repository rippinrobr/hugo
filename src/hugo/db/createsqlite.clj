(ns hugo.db.createsqlite
  (:use [clojure.contrib.sql :only (with-connection with-query-results)] )
  (:use hugo.db.sqlite)
  (:require [clojure.contrib.sql :as sql]))

; this makes use db connection in the sqlite.clj file
(def new-db-conn (merge db {:create true}))

(defn create-tables
 "Creates the tables needed to store the info about the award winners and nominees"
 []
 (sql/create-table
   :orgs
  [:id :integer "PRIMARY KEY"])

 (sql/create-table
   :categories
   [:id :integer "PRIMARY KEY"])

 (sql/create-table
   :nominees
   [:id :integer "PRIMARY KEY"]))

(defn create-db
 "Creates a new database"
 []
 (sql/with-connection new-db-conn
   (sql/transaction (create-tables))))
