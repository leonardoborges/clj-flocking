(ns clj-flocking.algo
  "Flocking algorithm ported from http://processingjs.org/learning/topic/flocking"
  (:use clj-flocking.vec.math
        clj-flocking.vec.helpers
        clj-flocking.defaults))


(defn mean-separation-vec [boid neighbours]
  (let [vecs (for [neighbour neighbours
                   :let [b @boid
                         n @neighbour
                         d (vec-eucl-distance (:location b) (:location n))]
                   :when (and (> d 0) (< d desired-separation))]
               (-> (vec-sub (:location b) (:location n))
                   (vec-normalize)
                   (vec-div d)))]
    {:mean-vec (reduce vec-add vecs) :count (count vecs)}))

(defn separate [boid neighbours]
  (let [{mean-vec :mean-vec count :count} (mean-separation-vec boid neighbours)]
    (if (> count 0)
      (vec-div mean-vec count)
      mean-vec)))

(defn mean-alignment-vec [boid neighbours]
  (let [vecs (for [neighbour neighbours
                   :let [b @boid
                         n @neighbour
                         d (vec-eucl-distance (:location b) (:location n))]
                   :when (and (> d 0) (< d neighbour-radius))]
               (:velocity n))]
    {:mean-vec (reduce vec-add vecs) :count (count vecs)}))


(defn limit [v max]
  (if (> (vec-magnitude v) max)
    (vec-mult (vec-normalize v) max)
    v))

(defn align [boid neighbours]
  (let [{mean-vec :mean-vec count :count} (mean-alignment-vec boid neighbours)]
    (if (> count 0)
      (limit (vec-div mean-vec count) max-force)
      (limit mean-vec max-force))))

(defn steer-to [boid target]
  (let [b @boid
        desired (vec-sub target (:location b))
        d (vec-magnitude desired)]
    (if (> d 0)
      (let [normalized-desired (vec-normalize desired)
            new-desired (if (< d 100.0)
                          (-> normalized-desired (vec-mult (* max-speed (/ d 100.0))))
                          (-> normalized-desired (vec-mult max-speed)))]
        (-> new-desired (vec-sub (:velocity b)) (limit max-force)))
      (mk-vec 0.0 0.0))))

(defn mean-coherence-vec [boid neighbours]
  (let [vecs (for [neighbour neighbours
                   :let [b @boid
                         n @neighbour
                         d (vec-eucl-distance (:location b) (:location n))]
                   :when (and (> d 0) (< d neighbour-radius))]
               (:location n))]
    {:mean-vec (reduce vec-add vecs) :count (count vecs)}))

(defn cohere [boid neighbours]
  (let [{mean-vec :mean-vec count :count} (mean-coherence-vec boid neighbours)]
    (if (> count 0)
      (steer-to boid (vec-div mean-vec count))
      mean-vec)))

(defn flock [boid neighbours]
  (let [separation (vec-mult (separate boid neighbours) weight-separation)
        alignment  (vec-mult (align boid neighbours) weight-alignment)
        cohesion   (vec-mult (cohere boid neighbours) weight-cohesion)]
    (vec-add separation alignment cohesion)))

(defn step [boid neighbours max-width max-height]
  (let [acceleration (flock boid neighbours)
        new-velocity (-> (:velocity @boid)
                         (vec-add acceleration)
                         (limit max-speed))
        new-location (-> (:location @boid)
                         (vec-add new-velocity)
                         (bound-location max-width max-height))]
    (swap! boid (fn [boid-map]
                  (assoc boid-map
                    :velocity new-velocity
                    :location new-location)))))