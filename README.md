# Lorenzo il Magnifico - GC_32
## How to build
#### Gradle
In order to build with gradle python3 is required for regenerating the cards json.
Other than that the compilation is strightforward
```
gradle build
```
Two jars will be produced under `build/libs`
* limg_server_GC_32.jar - the server binary
* limg_client_GC_32.jar - the client binary

both can be easily launched with
```
java -jar limg_[client/server]_GC_32.jar
```

#### Maven
Analogously to gradle the compilation is straighfforward
```
maven package
```
two jars will be created in the directory `target`, they can be launched in the same way as gradle's

## How to run
In order to run the complete game the server needs to be started first,
then the client.
