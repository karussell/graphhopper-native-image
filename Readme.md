# native image example

 * download graalvm (e.g graal CE for linux amd64, 19.3.0) and extract to `graalvm/`
 * `bin/gu install native-image` or: `bin/gu -L install ~/Downloads/native-image-installable-svm-java11-linux-amd64-19.3.0.jar`
 * modify pom.xml to include native image plugin, see this repo
 * set JAVA_HOME to the downloaded graal (`graalvm/`)
 * try `$JAVA_HOME/bin/native-image --help`
 * `mvn package # worked for a simple example!`
 * run `./target/example`