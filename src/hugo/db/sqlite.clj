(ns hugo.db.sqlite
  (:use [clojure.contrib.sql :only (with-connection with-query-results)] )
  (:require [clojure.contrib.sql :as sql]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"		; Protocol to use
	 :subname "db/hugo.sqlite3"	; Location of the db
         })

(def get-all-orgs "select * from orgs")

(defn get-sql 
  "Generic select statement functionality"
  [sql-stmt]
  (with-connection db
    (sql/with-query-results results sql-stmt
      (doall results))))

(defn add-org 
  [name]
  (with-connection db
    (sql/insert-values :orgs [:name] [name])))

(defn delete-org
  ([id]
    (with-connection db
      (sql/delete-rows :orgs ["id=?" id]))))
     
(defn get-org
  ([]
   (get-sql [get-all-orgs]))
  ([id]
    (get-sql [(str get-all-orgs " where id=?") id])))

(defn update-org
  ([id name]
    (with-connection db
      (sql/update-values :orgs ["id=?" id] {:name name}))))
