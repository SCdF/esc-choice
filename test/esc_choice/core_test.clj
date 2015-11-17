(ns esc-choice.core-test
  (:require [clojure.test :refer :all]
            [esc-choice.core :refer :all]
            [clj-time.core :as t]))

(defn dt [& params] (apply t/local-date-time (concat [2015 11 11] params)))
(defn t [& params] (apply t/local-time params))

(deftest time-contained-test
  (testing "time-contained? works generally"
    (is      (time-contained? (dt 11) [(t 10) (t 12 30)]))
    (is (not (time-contained? (dt 11) [(t 12) (t 14)]))))
  (testing "including across the midnight boundary"
    (is      (time-contained? (dt 11) [(t 10) (t 3)]))
    (is (not (time-contained? (dt 11) [(t 23) (t 3)])))
    (is      (time-contained? (dt 11) [(t 23) (t 12)])))
  (testing "time-contained? contains boundaries"
    (is      (time-contained? (dt 11 12 13) [(t 11 12 13) (t 12)]))
    (is      (time-contained? (dt 11 12 13) [(t 10) (t 11 12 13)]))))

;; TODO work out how to do a core.check test on time-contained? to catch edge-cases
