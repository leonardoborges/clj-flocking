(ns clj-flocking.vec.math
  (:use clj-flocking.vec.helpers))

(defn vec-add
  ([] (mk-vec 0.0 0.0))
  ([v1 v2]
     (mk-vec (+ (:x v1)
                (:x v2))
             (+ (:y v1)
                (:y v2))))
  ([v1 v2 & rest]
     (reduce vec-add (vec-add v1 v2) rest)))


(defn vec-sub [v1 v2]
  (mk-vec (- (:x v1)
             (:x v2))
          (- (:y v1)
             (:y v2))))

(defn vec-mult [v s]
  (mk-vec (* (:x v)
             s)
          (* (:y v)
             s)))

(defn vec-div [v s]
  (mk-vec (/ (:x v)
             s)
          (/ (:y v)
             s)))

(defn vec-magnitude [v]
  (Math/sqrt (+ (Math/pow (:x v) 2)
                (Math/pow (:y v) 2))))

(defn vec-normalize [v]
  (let [m (vec-magnitude v)]
    (if (> m 0)
      (vec-div v m)
      v)))

(defn vec-eucl-distance [v other]
  (Math/sqrt (+ (Math/pow (- (:x v)
                             (:x other)) 2)
                (Math/pow (- (:y v)
                             (:y other)) 2))))

(defn heading [velocity]
  (- (Math/atan2 (- (:y velocity)) (:x velocity))))