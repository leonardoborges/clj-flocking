(ns clj-flocking.vec.helpers
  (:use clj-flocking.defaults))

(defn mk-vec [x y]
  {:x x :y y})

(defn bound-location [location max-width max-height]
  (cond
   (< (:x location) (- radius)) (recur (assoc location :x (+ max-width radius))
                                       max-width max-height)
   (> (:x location) (+ max-width radius)) (recur (assoc location :x (- radius))
                                                 max-width max-height)
   (< (:y location) (- radius)) (recur (assoc location :y (+ max-height radius))
                                       max-width max-height)
   (> (:y location) (+ max-height radius)) (recur (assoc location :y (- radius))
                                                  max-width max-height)
   :else location))