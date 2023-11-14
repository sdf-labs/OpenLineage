# Instructions for Building the OpenLineage Jar

Note: Please run these instructions within an Intellij IDE, which helps to configure / install all of the gradle build dependencies & jars automatically

To install a clean jar, start by cleaning up your local maven cache:

```bash
sudo rm -r ~/.m2/repository
```

Next, navigate to `client/java` and run:

```bash
./gradlew clean build publishToMavenLocal
```

Once that completes successfully, go ahead and navigate to `integration/sql/iface-java` and run:

```
cargo clean
./script/compile.sh
./script/build.sh
```


Lastly, navigate to `integration/spark`. Run:

```
./gradlew clean --refresh-dependencies
```


You may run into an error if the `gradle.properties` defined version has a `-SNAPSHOT` suffix. Remove it, run the above command, and before proceeding further,ensure that you have added it back. Your `gradle.properties` should look as follows:

```gradle.properties
jdk8.build=true
version={YOUR-VERSION-NUMBER}-SNAPSHOT
spark.version=3.5.0
org.gradle.jvmargs=-Xmx1G
```

Finally run:

```
./gradlew build -x test
```

*Note:* This currently excludes tests as some are failing

The jar should be available at: `intergation/spark/build/libs/openlineage-spark-1.4.1-SNAPSHOT.jar`