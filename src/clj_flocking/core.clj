(ns clj-flocking.core
  "Flocking algorithm ported from http://processingjs.org/learning/topic/flocking"
  (:use [clj-flocking.ui]
        [clj-flocking.vec.helpers]
         [clj-flocking.defaults]
        [quil.core])
  (:gen-class))


;;(use 'clj-flocking.ui :reload)

(defn mk-boid [location velocity]
  (atom {:location location
         :velocity velocity
         }))

(defn mk-sample-boids [n]
  (take n (repeatedly #(mk-boid
                        (mk-vec (/ frame-width 2.0)
                                (/ frame-height 2.0))
                        (mk-vec (- (* (Math/random) 2) 1)
                                (- (* (Math/random) 2) 1))))))

(def boids (mk-sample-boids 100))


(defn -main
  [& args]
  (let [boids (mk-sample-boids 100)]
    (defsketch example
      :title "Clojure flocking simulation demo - written by Leonardo Borges - @leonardo_borges"
      :setup setup        
      :draw #(draw boids)
      :size [800 600])))

