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

(defn contained?
  [^DateTime instant time-range ^DateTimeZone user-tz]
  (localtime-contained? (-> instant
                            (.withZone user-tz)
                            .toLocalTime)
                        time-range))

(defn- available-in-priority
  [priority instant people]
  (filter (fn [person]
            (some
              #(contained? instant (rest %) (:tz person))
              (filter #(priority (first %)) (:availability person))))
  people))

(defn priorities->fn
  "Converts priority list into functions. If you don't pass a keyword, I sure
   hope it's a function!"
  [priorites] (map (fn [p] (if (keyword? p) (partial = p) p)) priorites))

;; TODO: [priorites instant people] or [priorities people instant] or?
;; TODO: is this actually lazy? test
(defn available
  "A lazy list of available people. Returns all people available in the first
  priority given, and then in the next and so on. People may appear multiple
  times, once for each priority"
  [priorities instant people]
  (mapcat #(available-in-priority % instant (shuffle people)) (priorities->fn priorities)))

; (defn choice-algo
;   [people current-time]
;   (let [time-contained? (partial time-contained? current-time)]
;     (-> people
;         (filter (comp time-contained? :availability))
;         rand-nth)))

