avl-crrcsim-editor
==================

An editor for AVL (Athena Vortex Lattice) and a exporter for Crrcsim an Open source model airplane simulator http://sourceforge.net/projects/crrcsim/

Install first avl 3.35 from here http://web.mit.edu/drela/Public/web/avl/

[Download](https://github.com/TLmaK0/avl-crrcsim-editor/releases/latest)

And select avl path in Edit->Set AVL executable

Create your airplane, and then export as AVL file or CRRCsim Xml.

This software is in a early beta fase, so be careful with the result.

Please, help me to improve it.


Building from sources on Debian/Ubuntu
--------------------------------------

Building avl-crrcsim-editor requires scala 2.10.4 with sbt 0.13.7

To install these in debian or ubuntu, download and install the .deb from scala-sbt.org:

    wget http://repo.scala-sbt.org/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.13.7/sbt.deb
    sudo dpkg -i sbt.deb

Of course you will need the avl-crrcsim-editor sources too:

    git clone https://github.com/TLmaK0/avl-crrcsim-editor.git

The first time you run sbt it will download and install a whole bunch of dependencies, which can take a long time on a slow connection. The following command will list the available tasks after bootstrapping the environment:

    cd avl-crrcsim-editor
    sbt tasks


