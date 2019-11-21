# native image example

 * download graalvm (e.g graal CE for linux amd64, 19.3.0) and extract to `graalvm/`
 * `bin/gu install native-image` or: `bin/gu -L install ~/Downloads/native-image-installable-svm-java11-linux-amd64-19.3.0.jar`
 * native-image needs a local C toolchain: glibc-devel, zlib-devel and gcc - e.g.`sudo apt-get install zlib1g-dev`
 * set JAVA_HOME to the downloaded graal (`graalvm/`)
 * try `$JAVA_HOME/bin/native-image --help`
 * `mvn clean package`
 * `wget http://download.geofabrik.de/europe/germany/berlin-latest.osm.pbf -O osm.pbf`
 * `./target/graphhopper`

If you are finished and you noticed something is eating your CPU resources do:

`$JAVA_HOME/bin/native-image --server-shutdown`

# Unscientific Results

Measured via command line utility `time` and picked the "real" time

 * OpenJDK: 0.5s
 * native image: <0.05s

# How to make resources of dependencies working?

Run application with GraalVM and special agent:

```
$JAVA_HOME/bin/java -agentlib:native-image-agent=config-output-dir=./ni-configs -jar target/*-jar-with-dependencies.jar
```

Then include these files in native-image command line: see maven plugin.

See also:

https://github.com/oracle/graal/blob/master/substratevm/CONFIGURE.md

https://github.com/oracle/graal/blob/master/substratevm/RESOURCES.md

# slf4j-log4j bridge fails with ClassNotFound

Currently we just use slf4j-simple. Likely this is fixed via the resources
config in previous section or maybe this: https://github.com/oracle/graal/issues/653

# Graal Help
 
 * https://www.graalvm.org/community/
 * https://github.com/graalvm/graalvm-demos
