import sbtprotobuf.{ProtobufPlugin=>PB}

seq(PB.protobufSettings: _*)

unmanagedResourceDirectories in Compile <+= (sourceDirectory in PB.protobufConfig)

version in PB.protobufConfig := "2.5.0"

organization  := "com.eveintel"

name := "ei-pb"

version := "1.0.0-SNAPSHOT"




