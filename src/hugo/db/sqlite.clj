(ns hugo.db.sqlite
  (:use [clojure.contrib.sql :only (with-connection with-query-results)] )
  (:require [clojure.contrib.sql :as sql]))

(def db {
  :classname "org.sqlite.JDBC"
  :subprotocol "sqlite"		; Protocol to use
  :subname "db/hugo.sqlite3"	; Location of the db
})

(def get-all-nominees "select * from nominees")

(defn get-sql 
  "Generic select statement functionality"
  [sql-stmt]
  (with-connection db
    (sql/with-query-results results sql-stmt
      (doall results))))

(defn add-nominee
  [year nominee]
   (with-connection db (sql/insert-values :nominees 
     [:year :title :author :winner] 
          [year (:title nominee) (:author nominee) 
           (if (nil? (:winner nominee)) 0 1)])))

(defn get-nominee
  ([id] (get-sql [(str get-all-nominees " where id=?") id])))

(defn get-nominees
  ([] (get-sql [get-all-nominees]))
  ([year] (get-sql [(str get-all-nominees " where year=?") year])))

(defn get-winners
  ([] (get-sql [(str get-all-nominees " where winner=1")]))
  ([year] (get-sql [(str get-all-nominees " where winner=1 and year=?") year])))
