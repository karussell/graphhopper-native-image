# Description

[GraphHopper](https://github.com/graphhopper/graphhopper) is a fast and memory efficient Java routing engine, released under Apache License 2.0. By default it uses OpenStreetMap and GTFS data, but it can import other data sources.

It runs on Android and iOS, i.e. it calculates the routes on the device without internet access. But there are a couple of problems with each approach:

 * GraphHopper [mostly already works on Android](https://github.com/graphhopper/graphhopper/tree/master/android) but Android does not support the latest Java. Even Java 8 is not really well supported. This forces us to stay on old dependencies for our `core` module and also on Java7 features. Android additionally limits the area one process can mmap. This is ugly because it is an arbitrary limit at ~1.5GB hardcoded in the sources although the devices usually have more and there is no such limit for native applications.
 * There is an [iOS port](https://github.com/graphhopper/graphhopper-ios/) using j2objc, but maintaining this port is not trivial. And so this port is not up-to-date.

# Solutions

 * Rewrite in Kotlin that can target iOS and Android and even JavaScript. But this is a huge work and on Android one needs an additional Kotlin dependency.
 * GraalVM looks promising and could help us to develop a native library that could be included on iOS and Android. 
 
And this repository is about the GraalVM option. Or at least to get a native library working for now on currently supported platforms like Linux.

# Web

## Create Native Image for GraphHopper Web

 * [download graalvm](https://github.com/graalvm/graalvm-ce-builds/releases/)
   for your OS e.g graal CE for linux amd64, 19.3.0 and extract to `graalvm/`
 * run `bin/gu install native-image`
 * The native-image tool needs a local C toolchain: glibc-devel, zlib-devel and gcc - do e.g.`sudo apt-get install zlib1g-dev`
 * verify that the tool works: `$JAVA_HOME/bin/native-image --help`
 * set JAVA_HOME to the downloaded graal (`graalvm/`) to use the correct JVM for maven
 * `cd web`
 * `mvn clean package` => should create a native image at ./target/graphhopper

## Start the GraphHopper Web Native Image

Now that graal created your native image you can start graphhopper:

 * `cd web`
 * download OSM road data: `wget http://download.geofabrik.de/europe/germany/berlin-latest.osm.pbf -O osm.pbf`
 * start GraphHopper: `./target/graphhopper`, see example.cpp and src/main/java/example/Example.java for the code that will be executed

It will take a few seconds where it basically converts the pbf
file into an internal graph structure (see graph-cache folder), but all subsequent
calls will be very fast and calculate the distance between two points in
Berlin (see java code).

If you are finished and you noticed something is eating your CPU resources do:

`$JAVA_HOME/bin/native-image --server-shutdown`

## Unscientific Results

Measured via command line utility `time` and picked the "real" time

 * OpenJDK: 0.5s
 * native image: <0.05s
 * shared library: ~0.025s
 
## Future Tasks

 * target iOS, see target=iOS here: https://github.com/gluonhq/client-samples and https://github.com/gluonhq/substrate/issues/47
 * target Android, see https://github.com/gluonhq/client-samples/issues/35
 * we could try to start the dropwizard web service as native image and compare e.g. performance
 
## How to make resources of dependencies working?

Run application with GraalVM and special agent:

```
$JAVA_HOME/bin/java -agentlib:native-image-agent=config-output-dir=./ni-configs -jar target/*-jar-with-dependencies.jar
```

Then include these files in native-image command line: see maven plugin.

See also:

https://github.com/oracle/graal/blob/master/substratevm/CONFIGURE.md

https://github.com/oracle/graal/blob/master/substratevm/RESOURCES.md

## Open Questions

 * slf4j-log4j bridge fails with ClassNotFound. Workaround: use slf4j-simple. Likely this is fixed via the resources config in previous section or maybe
   [this](https://github.com/oracle/graal/issues/653)
 * Why are they e.g. libgraphhopper_dynamic.h headers and how can I use 
   `--static` as then no headers are created?

## Call GraphHopper from C++

Instead of a native application we now want a library that is called from
native code like example.cpp. To create this library do the following:

 * use `<imageName>libgraphhopper</imageName>` and add `--shared` in `<buildArgs>` in pom.xml
 * `mvn clean package`

Now you should see libgraphhopper.so, libgraphhopper.h, graal_isolate.h in the
./target folder. Proceed with:

```
g++ example.cpp -I ./target -L ./target -lgraphhopper -o target/example
export LD_LIBRARY_PATH=./target:$LD_LIBRARY_PATH && ./target/example 52.5147 13.3
[main] INFO com.graphhopper.reader.osm.GraphHopperOSM - version 1.0|2019-11-20T22:11:41Z (5,15,4,3,5,5)
[main] INFO com.graphhopper.reader.osm.GraphHopperOSM - graph CH|car|MMAP_STORE|2D|no_turn_cost|5,15,4,3,5, details:edges:126,973(4MB), ...
Distance 6806.26
```

# Graal Help
 
 * installing the native-image plugin fails? Download the jar, then do `bin/gu -L install ~/Downloads/native-image-installable-svm-java11-linux-amd64-19.3.0.jar`
 * https://www.graalvm.org/community/
 * https://github.com/graalvm/graalvm-demos
 * https://github.com/neomatrix369/awesome-graal
 * [travis example config for GraalVM](https://github.com/gleb-kosteiko/latepost-scheduler/blob/master/.travis.yml)
 * Java distribution of native C++ libraries https://github.com/bytedeco/javacpp-presets
