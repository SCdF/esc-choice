(ns esc-choice.core
  (:require [clj-time.core :as t])
  (:import [org.joda.time DateTime DateTimeZone LocalTime]))

(defn localtime-contained?
  "Is the instant contained in the defined range?"
  [^LocalTime instant-time [^LocalTime start-time ^LocalTime end-time]]
  (or (= instant-time start-time)
      (= instant-time end-time)
      (if (t/before? start-time end-time)
          (and (t/before? start-time instant-time) (t/after? end-time instant-time))
          (or (and (t/before? end-time instant-time) (t/before? start-time instant-time))
              (and (t/after? end-time instant-time) (t/after? start-time instant-time))))))

(defn time-contained?
  [^DateTime instant time-range ^DateTimeZone user-tz]
  (localtime-contained? (-> instant
                            (.withZone user-tz)
                            .toLocalTime)
                        time-range))


; (defn choice-algo
;   [people current-time]
;   (let [time-contained? (partial time-contained? current-time)]
;     (-> people
;         (filter (comp time-contained? :availability))
;         rand-nth)))

