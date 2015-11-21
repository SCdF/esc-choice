(ns esc-choice.core-test
  (:require [clojure.test :refer :all]
            [esc-choice.core :refer :all]
            [clj-time.core :as t])
  (:import [org.joda.time DateTimeZone]))

;; Utils
(defn tz [tz-string] (t/time-zone-for-id tz-string))
(defn dt [tz-string & params] (t/from-time-zone
  (apply t/date-time (concat [2015 11 11] params))
  (tz tz-string)))
(defn ldt [& params] (apply t/local-date-time (concat [2015 11 11] params)))
(defn lt [& params] (apply t/local-time params))

;; Tests
(deftest localtime-contained-test
  (testing "localtime-contained? works generally"
    (is      (localtime-contained? (lt 11) [(lt 10) (lt 12 30)]))
    (is (not (localtime-contained? (lt 11) [(lt 12) (lt 14)]))))
  (testing "including across the midnight boundary"
    (is      (localtime-contained? (lt 11) [(lt 10) (lt 3)]))
    (is (not (localtime-contained? (lt 11) [(lt 23) (lt 3)])))
    (is      (localtime-contained? (lt 11) [(lt 23) (lt 12)])))
  (testing "localtime-contained? contains boundaries"
    (is      (localtime-contained? (lt 11 12 13) [(lt 11 12 13) (lt 12)]))
    (is      (localtime-contained? (lt 11 12 13) [(lt 10) (lt 11 12 13)]))))

(deftest contained?-test
  (testing "contained? works generally"
    (is      (contained? (dt "UTC" 10 30) [(lt 10) (lt 12)] (tz "UTC"))))
  (testing "contained? understands timezones!"
    (is (not (contained? (dt "UTC" 10 30) [(lt 10) (lt 12)] (tz "NZ"))))
    (is      (contained? (dt "UTC" 10 30) [(lt 22) (lt 0)] (tz "NZ")))))

(def test-person-NZ {
  :tz "NZ"
  :availability [
    [(lt 9) (lt 12)]
    [(lt 13) (lt 18)]]})
(def test-person-UK {
  :tz "Europe/London"
  :availability [
    [(lt 9) (lt 12)]
    [(lt 13) (lt 22)]]})
(def test-people [test-person-NZ test-person-UK])

(deftest available-test
  (testing "available returns a [lazy] list of all available people"
    (is [test-person-UK] (available (dt "UTC" 10) test-people))
    (is [test-person-NZ] (available (dt "UTC" 3) test-people))
    (is test-people (available (dt "UTC" 20) test-people))))
;; TODO work out how to do a core.check test on localtime-contained? to catch edge-cases
