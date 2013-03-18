import sbtprotobuf.{ProtobufPlugin=>PB}

unmanagedResourceDirectories in Compile <+= (sourceDirectory in PB.protobufConfig).identity



PB.protobufSettings

version in PB.protobufConfig := "2.5.0"

organization  := "com.eveintel"

name := "eiprotobuf"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.9.2"



