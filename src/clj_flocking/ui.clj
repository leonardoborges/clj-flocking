(ns clj-flocking.ui
  (import (javax.swing JFrame JPanel)
          (java.awt BorderLayout
                    Color
                    Dimension
                    RenderingHints)
          java.awt.image.BufferedImage
          java.awt.geom.GeneralPath
          (java.awt.event MouseAdapter MouseEvent))
  (:use clj-flocking.vec.math))

(defn mk-main-frame [width height]
  (doto (JFrame. "Clojure flocking simulation demo - written by Leonardo Borges")
    (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
    (.setLayout (BorderLayout.))
    (-> (.getContentPane) (.setPreferredSize (Dimension. width height)))
    (.pack)
    (.setResizable false)
    (.setLocationRelativeTo nil)))

(defn draw-boid [g boid radius]
  (let [b @boid
        boid-location (:location b)
        boid-shape (doto (GeneralPath.)
                     (.moveTo (float 0)
                              (float 0))
                     (.lineTo (float radius)
                              (float (* radius 2)))
                     (.lineTo (float (- radius))
                              (float (* radius 2)))
                     (.lineTo (float 0)
                              (float 0)))]
    (let [theta (+ (heading (:velocity b))
                   (Math/toRadians 90))
          transform (.getTransform g)]
      (doto g
        (.setRenderingHint java.awt.RenderingHints/KEY_ANTIALIASING
                           java.awt.RenderingHints/VALUE_ANTIALIAS_ON)
        (.translate (int (:x boid-location)) (int (:y boid-location)))
        (.rotate theta)
        (.setPaint Color/BLACK)        
        (.draw boid-shape)
        (.setPaint Color/BLUE)        
        (.fill boid-shape)
        (.setTransform transform)))))

(defn mk-panel [boids boid-radius frame-width frame-height]
  (doto (proxy [JPanel] []
          (paint [g]
            (let [img (new BufferedImage frame-width frame-height
                           (. BufferedImage TYPE_INT_ARGB))
                  bg (. img (createGraphics))]
              (doto bg
                (.setColor (. Color white))
                (.fillRect 0 0 (. img (getWidth)) (. img (getHeight))))
              (doseq [b boids]
                (draw-boid bg b boid-radius))
              (. g (drawImage img 0 0 nil))
              (. bg (dispose)))))
    (.setPreferredSize (new Dimension 
                            frame-width
                            frame-height))))

(defn add-mouse-adapter [panel agent animate-fn running]
  (let [adapter (proxy [MouseAdapter] []
                  (mouseClicked [e]
                    (if (= (.getButton e) MouseEvent/BUTTON1)
                      (if @running
                        (swap! running (fn [_] false))
                        (do (swap! running (fn [_] true))
                            (send-off agent animate-fn))))))]
    (.addMouseListener panel adapter)))