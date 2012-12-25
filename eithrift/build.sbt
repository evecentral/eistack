import com.twitter.sbt._

seq(CompileThriftScrooge.newSettings: _*)

CompileThriftScrooge.scroogeVersion := "3.0.1"


name := "eithrift"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.9.2"

resolvers += "Twitter" at "http://maven.twttr.com/"

libraryDependencies += "org.apache.thrift" % "libthrift" % "0.8.0"

libraryDependencies += "com.twitter" %% "scrooge-runtime" % "3.0.1"

libraryDependencies += "com.twitter" % "finagle-thrift" % "5.3.22"

