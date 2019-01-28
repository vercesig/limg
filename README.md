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

# License
Copyright 2017 Alessandro Palummo, Enrico Ruggiano, Giacomo Vercesi.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Lorenzo il Magnifico game, card images and visual assets are copyright of Cranio Creations. 