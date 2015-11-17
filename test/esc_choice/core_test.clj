(ns esc-choice.core-test
  (:require [clojure.test :refer :all]
            [esc-choice.core :refer :all]
            [clj-time.core :as t]))

(defn dt [& params] (apply t/local-date-time (concat [2015 11 11] params)))
(defn t [& params] (apply t/local-time params))

(deftest time-contained-test
  (testing "time-contained? works generally, including across midnight"
    (is      (time-contained? (dt 11) [(t 10) (t 12 30)]))
    (is (not (time-contained? (dt 11) [(t 12) (t 14)])))
    (is      (time-contained? (dt 11) [(t 10) (t 3)])))
  (testing "time-contained? contains boundaries"
    (is      (time-contained? (dt 11 12 13) [(t 11 12 13) (t 12)]))
    (is      (time-contained? (dt 11 12 13) [(t 10) (t 11 12 13)]))))
