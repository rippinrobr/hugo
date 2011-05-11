(ns hugo.db.sqlite
  (:use [clojure.contrib.sql :only (with-connection with-query-results)] )
  (:require [clojure.contrib.sql :as sql]))

(def db {:classname "org.sqlite.JDBC"
         :subprotocol "sqlite"		; Protocol to use
	 :subname "db/hugo.sqlite3"	; Location of the db
         })

(def get-all-orgs "select * from orgs")
(def get-all-cats "select * from categories")
(def get-all-nominees "select * from nominees")

(defn get-sql 
  "Generic select statement functionality"
  [sql-stmt]
  (with-connection db
    (sql/with-query-results results sql-stmt
      (doall results))))

;--------------------
; orgs table
;--------------------
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
    (first (get-sql [(str get-all-orgs " where id=?") id]))))

(defn get-org-id
  ([name]
    (:id (first (get-sql [(str get-all-orgs " where name=?") name])))))

(defn update-org
  ([id name]
    (with-connection db
      (sql/update-values :orgs ["id=?" id] {:name name}))))

;-------------------
; categories table
;-------------------
(defn add-category
  "Adds a new category to the categories table after ensuring the
   org-id exists."
  [org-id name]
  (if (not (nil? (get-org org-id)))
    (with-connection db (sql/insert-values :categories [:org_id :name] [org-id name]))
    (println (str "The org-id " org-id " does not exist"))))

(defn delete-category
  ([id]
    (with-connection db
      (sql/delete-rows :categories ["id=?" id]))))
     
(defn get-category
  ([]
   (get-sql [get-all-cats]))
  ([id]
    (first (get-sql [(str get-all-cats " where id=?") id]))))

(defn get-category-id
  ([org-id name]
    (:id (first 
      get-sql [(str get-all-cats " where org_id=? and name=?") org-id name])))))

(defn update-category
  ([id name]
    (with-connection db
      (sql/update-values :categories ["id=?" id] {:name name})))
  ([id org-id name]
    (with-connection db
      (sql/update-values :categories ["id=?" id] {:org_id org-id :name name}))))

;-------------------
; nominees table
;-------------------
(defn add-nominee
  "Adds a new nominee to the nominees table after ensuring the
   org-id and cat-id exists."
  [org-id cat-id year title author]
  (if (not (nil? (get-org org-id)))
    (if (not (nil? (get-category cat-id))) 
      (with-connection db 
        (sql/insert-values :nominees 
          [:org-id :cat-id :year :title :author] [org-id cat-id year title author]))
      (println (str "The cat-id " cat-id " does not exist")))
    (println (str "The org-id " org-id " does not exist"))))

(defn delete-nominee
  ([id]
    (with-connection db
      (sql/delete-rows :nominees ["id=?" id]))))
     
(defn get-nominee
  ([]
   (get-sql [get-all-nominees]))
  ([id]
    (get-sql [(str get-all-nominees " where id=?") id])))

(defn get-category-nominees
  ([cat-id]
    (get-sql [(str get-all-nominees " where category_id=?") cat-id])))

(defn get-category-winners
  ([cat-id]
    (get-sql [(str get-all-nominees " where category_id=? and winner=1") cat-id])))

(defn update-nominee
  ([id title author]
    (with-connection db
      (sql/update-values :nominees ["id=?" id] {:title title :author author})))
  ([id read-it own-it want-it]
    (with-connection db
      (sql/update-values :nominees ["id=?" id] {:read_it read-it :own_it own-it :want_it want-it})))
  ([id org-id cat-id title author]
    (with-connection db
      (sql/update-values :nominees ["id=?" id] {:org_id org-id :category_id cat-id :title title :author author}))))
