(ns clojure-spanish-date-parser.core
  (:gen-class)
  (:require [java-time.api :as jt]
            [clojure.string :as str]))

(def ^:const ^:private date-separator "(?:\\.|\\/|-|—|,)")
(def ^:const ^:private day-regex "(?:[1-9]|1[0-9]|2[0-9]|3[0-1]|0[0-9])")

(defn- get-numbers [s]
  (->> (re-find (re-pattern (str/join date-separator (repeat 3 "(\\d+)"))) s)
       rest
       (map #(Integer/parseInt %))))

(defn- or-regex [coll]
  (str "(?:" (str/join "|" coll) ")"))

(defn- my-format [format [y m d]]
  (jt/format format (jt/local-date y m d)))

; regex-1

(def ^:const ^:private regex1
  "The above code is defining a regular expression pattern for matching dates in the format of
   day/month/year. The pattern allows for various delimiters such as \".\", \"/\", \"-\", \"—\", or \",\"
   between the day, month, and year components. The day component can be any number from 1 to 31,
   the month component can be any number from 1 to 12, and the year component can be any number
   from 0000 to 2999."
  (re-pattern (str day-regex date-separator "{1}\\s?(?:[1-9]|1[0-2]|0[0-9])" date-separator "{1}(?:20[0-9][0-9]|9[0-9]|1[0-9]|0[0-9]|2[0-9])")))

(defn- regex-1-parser [s]
  (reverse (get-numbers s)))

; regex-2

(def ^:const ^:private regex2
  "The above code is defining a regular expression pattern for matching dates in the format
  \"YYYY-MM-DD\" or \"YYYY/MM/DD\" or \"YYYY.MM.DD\" or \"YYYY-DD-MM\" or \"YYYY,MM,DD\". The pattern matches
  years starting with \"20\" followed by any two digits, followed by a separator character (such as
  \".\", \"/\", \"-\", \"—\", or \",\"), followed by a month number between 1 and 12, followed by another
  separator character, followed by a day number between 1 and 31."
  (re-pattern (str "20[0-9][0-9]" date-separator "{1}(?:[1-9]|1[0-2]|0[0-9])" date-separator "{1}" day-regex)))

(def ^:private regex-2-parser get-numbers)

; regex-3

(def ^:const ^:private month-names
  ["enero",
   "febrero",
   "marzo",
   "abril",
   "mayo",
   "junio",
   "julio",
   "agosto",
   "septiembre",
   "octubre",
   "noviembre",
   "diciembre"])

(def ^:const ^:private regex3
  "The above code is defining a regular expression pattern for matching dates in a specific format. The
  pattern is constructed using the month names provided in the `month_names` list. The pattern matches
  a day (1-31) followed by an optional period, followed by a month name, followed by an optional
  period, followed by a year (2000-2999)."
  (re-pattern (str day-regex
                   "\\.?(?:\\sde\\s|\\s)"
                   (or-regex month-names)
                   "(?:\\sde\\s|\\s|\\sdel\\s)(?:2\\.?0[0-9][0-9]|9[0-9]|1[0-9]|0[0-9]|2[0-9])")))

(defn- regex-3-parser [s]
  (let [s (str/replace s #"(?:\sdel|\sde)" "")
        [d mont-str y] (str/split s #" ")]
    [(Integer/parseInt y)
     (inc (.indexOf month-names mont-str))
     (Integer/parseInt d)]))

;;;; Regex 4

(def ^:const ^:private
  month-names-abbr ["ene",
                    "feb",
                    "mar",
                    "abr",
                    "may",
                    "jun",
                    "jul",
                    "ago",
                    "sep",
                    "oct",
                    "nov",
                    "dic"])

(def ^:const ^:private regex-4-separator "[^a-zA-Z0-9_]")

(def ^:const ^:private regex-4
  "The above code is defining a regular expression pattern for matching dates in a specific format.
  The pattern is constructed using the month names in abbreviated form (e.g. Jan, Feb, Mar) and
  allows for various date formats such as \"01-Jan-2022\" or \"1/Feb/2022\"."
  (re-pattern (str day-regex regex-4-separator (or-regex month-names-abbr) regex-4-separator "\\s?(?:2\\.?0[0-9][0-9]|9[0-9]|1[0-9]|0[0-9]|2[0-9])\\b")))

(defn- regex-4-parser [s]
  (let [[_ day month-str year] (re-matches (re-pattern (str/join regex-4-separator (repeat 3 "(.*)"))) s)]
    [(Integer/parseInt year)
     (inc (.indexOf month-names-abbr month-str))
     (Integer/parseInt day)]))

(defn- parse-initial-string [s]
  (-> s
      str/trim
      str/lower-case))

(defn date-parser [s & [format]]
  (let [s' (parse-initial-string s)]
    (if-let [f (condp re-matches s'
                 regex1 regex-1-parser
                 regex2 regex-2-parser
                 regex3 regex-3-parser
                 regex-4 regex-4-parser
                 nil)]
      (my-format (or format "dd/MM/yyyy") (f s'))
      s)))
