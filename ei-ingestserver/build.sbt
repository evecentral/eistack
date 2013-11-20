organization  := "com.eveintel"

name := "ei-ingestserver"

version := "1.0.0-SNAPSHOT"

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "joda-time" % "joda-time" % "2.1"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "com.google.guava" % "guava" % "14.0"

libraryDependencies += "com.google.code.findbugs" % "jsr305" % "2.0.1"


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.2"

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.7.2"


