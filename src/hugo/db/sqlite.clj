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

(defn get-nominees
  ([] (get-sql [get-all-nominees]))
  ([year] (get-sql [(str get-all-nominees " where year=?") year])))

(defn get-years []
  (get-sql ["select distinct year from nominees order by year desc"]))