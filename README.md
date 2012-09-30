# clj-flocking

A boids flocking simulation written in Clojure ported from [http://processingjs.org/learning/topic/flocking](http://processingjs.org/learning/topic/flocking)

## Dependencies

* [leiningen 2.x](https://github.com/technomancy/leiningen)
* [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - It should work with earlier but reasonably new versions of Java as well but I only tested it on Java 7

## Running

Create the standalone jar using [leiningen](https://github.com/technomancy/leiningen):

    $ lein uberjar

Now you're ready to run it:

    $ java -jar clj-flocking-0.1.0-standalone.jar


You can also run the clojure namespace directly:

    $ lein run -m clj-flocking.core

Alternatively you can go to the [Downloads](https://github.com/leonardoborges/clj-flocking/downloads) page and get a compiled jar ready to run.

## Options

Once the animation starts running, you can pause it by clicking with the mouse anywhere on the window. To start from scratch, restart the program.


## License

Copyright © 2012 Leonardo Borges - [www.leonardoborges.com](http://www.leonardoborges.com)

Distributed under the Eclipse Public License, the same as Clojure.
