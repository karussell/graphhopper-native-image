# native image example

 * download graalvm (e.g graal CE for linux amd64, 19.3.0) and extract to `graalvm/`
 * `bin/gu install native-image` or: `bin/gu -L install ~/Downloads/native-image-installable-svm-java11-linux-amd64-19.3.0.jar`
 * native-image needs a local C toolchain: glibc-devel, zlib-devel and gcc - e.g.`sudo apt-get install zlib1g-dev`
 * set JAVA_HOME to the downloaded graal (`graalvm/`)
 * try `$JAVA_HOME/bin/native-image --help`
 * `mvn package`
 * wget http://download.geofabrik.de/europe/germany/berlin-latest.osm.pbf -O osm.pbf
 * `./target/example`

If you are finished and you noticed something is eating your resources do:

`$JAVA_HOME/bin/native-image --server-shutdown`

# Requirements

Currently you need a small patch to apply to GraphHopper: https://gist.github.com/karussell/84d29b3254a6c92a8122c5737ebdc2c8

# How to make resources of dependencies working?

This seem to work via config or command line: https://github.com/oracle/graal/blob/master/substratevm/RESOURCES.md

But only for the project its files. Not for resources of one of the dependencies!?

# Why slf4j-log4j bridge fails with ClassNotFound

TODO: currently we just use slf4j-simple

# Graal Help
 
 * https://www.graalvm.org/community/
 * https://github.com/graalvm/graalvm-demos
 * Automatically create the configuration via java agent https://github.com/oracle/graal/blob/master/substratevm/CONFIGURE.md