import sbt._
import Keys._

object Build extends sbt.Build {
  lazy val root = Project(id = "eistack",
    base = file(".")).aggregate(eicore, eiprotobuf, eiingest).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

  lazy val eiprotobuf = Project(id = "ei-pb",
    base = file("ei-pb")).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)
  lazy val eicore = Project(id = "ei-km-core",
    base = file("ei-km-core")).dependsOn(eiprotobuf).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)
  lazy val eiingest = Project(id = "ei-ingestserver",
    base = file("ei-ingestserver")).dependsOn(eicore).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

}
