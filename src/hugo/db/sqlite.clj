(ns hugo.db.sqlite
  (:use [clojure.contrib.sql :only (with-connection with-query-results)] )
  (:require [clojure.contrib.sql :as sql]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"		; Protocol to use
	 :subname "db/hugo.sqlite3"	; Location of the db
         })
