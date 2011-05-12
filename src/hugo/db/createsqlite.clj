(ns hugo.db.createsqlite
  (:use [clojure.contrib.sql :only (with-connection with-query-results)] )
  (:use hugo.db.sqlite)
  (:require [clojure.contrib.sql :as sql]))

; this makes use db connection in the sqlite.clj file
(def new-db-conn (merge db {:create true}))

(defn drop-tables
  []
  (sql/with-connection new-db-conn
   (sql/drop-table :nominees)
   (sql/drop-table :categories)
   (sql/drop-table :orgs)))

(defn create-tables
 "Creates the tables needed to store the info about the award winners and nominees"
 []
 (drop-tables)
 (sql/create-table
   :orgs
  [:id :integer "PRIMARY KEY"]
  [:name "varchar(32)"])

 (sql/create-table
   :categories
   [:id :integer "PRIMARY KEY"]
   [:org_id :integer]
   [:name "varchar(64)"]
   ["FOREIGN KEY(org_id) REFERENCES orgs(id)"])

 (sql/create-table
   :nominees
   [:id :integer "PRIMARY KEY"]
   [:org_id :integer]
   [:category_id :integer]
   [:year :integer]
   [:title "varchar(64)"]
   [:author "varchar(32)"]
   [:winner "tinyint"]
   [:read_it "tinyint"]
   [:own_it "tinyint"]
   [:want_it "tinyint"]
   ["FOREIGN KEY(org_id) REFERENCES orgs(id)"]
   ["FOREIGN KEY(category_id) REFERENCES categories(id)"]))

(defn create-db
 "Creates a new database"
 []
 (sql/with-connection new-db-conn
   (sql/transaction (create-tables))))

