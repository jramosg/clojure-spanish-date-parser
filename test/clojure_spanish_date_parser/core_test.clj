(ns clojure-spanish-date-parser.core-test
  (:require [clojure.test :refer :all]
            [clojure-spanish-date-parser.core :refer [date-parser]]))

(deftest date-parser-test
  ;regex 1 test
  (is (= (date-parser "01.04.2023") "01/04/2023"))
  (is (= (date-parser "1.4.2023") "01/04/2023"))
  (is (= (date-parser "1/4/2023") "01/04/2023"))
  (is (= (date-parser "1-4-2023") "01/04/2023"))
  (is (= (date-parser "1,4,2023") "01/04/2023"))
  ;regex 2 test
  (is (= (date-parser "2023/10/04") "04/10/2023"))
  (is (= (date-parser "2023/10/4") "04/10/2023"))
  (is (= (date-parser "2023.10.4") "04/10/2023"))
  ;regex 3 test
  (is (= (date-parser "1 Enero 2023") "01/01/2023"))
  (is (= (date-parser "1 de enero de 2023") "01/01/2023"))
  (is (= (date-parser "01 de enero de 2023") "01/01/2023"))
  (is (= (date-parser "01 enero de 2023") "01/01/2023"))
  (is (= (date-parser "01 enero del 2023") "01/01/2023"))
  (is (= (date-parser "01 de enero del 2023") "01/01/2023"))
  ;regex 4 test
  (is (= (date-parser "01 Feb 2023") "01/02/2023"))
  (is (= (date-parser "1 Feb 2023") "01/02/2023"))
  (is (= (date-parser "1-Dic-2023") "01/12/2023"))
  (is (= (date-parser "1/Dic/2023") "01/12/2023"))

  ;no matching
  (is (= (date-parser "blablabla") "blablabla")))

(run-tests)
