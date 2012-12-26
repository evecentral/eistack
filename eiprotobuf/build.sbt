import sbtprotobuf.{ProtobufPlugin=>PB}

seq(PB.protobufSettings: _*)

name := "eiprotobuf"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.9.2"



