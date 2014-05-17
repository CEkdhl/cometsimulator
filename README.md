N-body simulator
==============
This repository contains an N-body simulator written in Java. It was originally used to simulate celestial bodies. Comets in the Oort cloud, in star clusters and things like that. In order to get acquainted with it, please refer to the following documentation:

- [Introduction & example simulations](http://cekdhl.github.io/cometsimulator/)
- [Case study: Simulating a star cluster](http://cekdhl.github.io/cometsimulator/starcluster.html)
- [Javadoc](http://cekdhl.github.io/cometsimulator/javadoc/)

Setup
==============
In order to compile this code you will need to include [Apache Commons Math](http://commons.apache.org/proper/commons-math/) in your project. We owe a great debt to Apache Commons Math because the underlying ODE solver comes from this package. In order to compile visualization.java you will need to include [Java3D](https://java3d.java.net/binary-builds.html).