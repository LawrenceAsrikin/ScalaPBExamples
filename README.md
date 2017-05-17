# ScalaPBExamples

**Instructions**

1). Create generated Scala classes based on your .proto file

    sbt compile
    
2). Run sample code in Main method

    sbt run



**Directory Structure**

1). Protobuf (.proto) config file location

    /src/main/protobuf/<PACKAGE NAME>/<PROTOBUF CONFIG NAME>.proto

2). Generated files location

    /target/scala-2.12/src_managed/main/<PACKAGE NAME>/<PROTOBUF CONFIG NAME>