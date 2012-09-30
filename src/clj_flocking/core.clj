(ns clj-flocking.core
  "Flocking algorithm ported from http://processingjs.org/learning/topic/flocking"
  (:use [clj-flocking.ui]
        [clj-flocking.vec.helpers]
        [clj-flocking.vec.math]
        [clj-flocking.algo]
        [clj-flocking.defaults])
  (:gen-class))

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




;; (swap! (:running @flocking-agent) (fn [old] false))
(defn animate [world]
  (while @(:running world)
    (do (. Thread sleep 10)
        (.repaint (:panel world))
        (doseq [boid (:boids world)]
          (step boid (:boids world)
                (-> world :panel (.getWidth))
                (-> world :panel (.getHeight))))))
  world)


(defn mk-world [boids panel]
  {:running (atom true)
   :boids boids
   :panel panel})

(defn -main
  [& args]
  (let [boids (mk-sample-boids 100)
        frame (mk-main-frame frame-width frame-height)
        panel (mk-panel boids radius frame-width frame-height)
        world (mk-world boids panel)
        flocking-agent (agent world)]
    (do
      (doto frame
        (-> (.getContentPane) (.add panel))
        (.setVisible true))
      (add-mouse-adapter panel flocking-agent animate (:running world))
      (send-off flocking-agent animate))))

