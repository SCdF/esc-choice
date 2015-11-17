(ns esc-choice.core
  (:require [clj-time.core :as t])
  (:import [org.joda.time LocalDateTime LocalTime]))

(defn time-contained?
  "Is the instant contained in the defined range?
  NB: intentionally not dealing with the time begin the exact same as start
  or end to see if we can use clojure-check (or whatever) correctly!"
  [^LocalDateTime instant [^LocalTime start-time ^LocalTime end-time]]
  (let [instant-time (.toLocalTime instant)]
    (or
      (= instant-time start-time)
      (= instant-time end-time)
      (if (t/before? start-time end-time)
            (and (t/before? start-time instant-time) (t/after? end-time instant-time))
            (or (and (t/before? end-time instant-time) (t/before? start-time instant-time))
                (t/after? start-time instant-time))))))

; (defn choice-algo
;   [people current-time]
;   (let [time-contained? (partial time-contained? current-time)]
;     (-> people
;         (filter (comp time-contained? :availability))
;         rand-nth)))

