(ns clj-flocking.ui
  (import (javax.swing JFrame JPanel)
          (java.awt BorderLayout
                    Color
                    Dimension
                    RenderingHints)
          java.awt.image.BufferedImage
          java.awt.geom.GeneralPath
          (java.awt.event MouseAdapter MouseEvent))
  (:use clj-flocking.vec.math
        clj-flocking.algo
        clj-flocking.defaults
        quil.core))

(defn setup []
  (smooth)                 
  (frame-rate 100)         
  (background 255 255 255))

(defn draw [boids]
  (scale 1)
  (background 255 255 255)
  (stroke 0 0 0)
  (stroke-weight 1)
  (fill 57 27 224) 

  (doseq [boid boids]
    (step boid boids (width) (height))
    (let [b @boid
          theta (+ (heading (:velocity b))
                   (Math/toRadians 90))
          boid-location (:location b)]
      (push-matrix)
      (translate  (int (:x boid-location)) (int (:y boid-location)))
      (rotate theta)
      (triangle 0 (- (* radius 2))
                (- radius) (* radius 2)
                radius (* radius 2))
      (pop-matrix))))

