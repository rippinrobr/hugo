(defproject hugo "0.0.4"
  :description "HUGO: Retrieves the Hugo Award winners and stores them in a sqlite db"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [sqlitejdbc "0.5.6"]
                 [enlive "1.0.0"]]
  :main cmdline)
