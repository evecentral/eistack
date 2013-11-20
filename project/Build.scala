import sbt._
import Keys._

object Build extends sbt.Build {
  lazy val root = Project(id = "eistack",
    base = file(".")).aggregate(eicore, eiprotobuf, eiingest)

  lazy val eiprotobuf = Project(id = "ei-pb",
    base = file("ei-pb"))
  lazy val eicore = Project(id = "ei-km-core",
    base = file("ei-km-core")).dependsOn(eiprotobuf)
  lazy val eiingest = Project(id = "ei-ingestserver",
    base = file("ei-ingestserver")).dependsOn(eicore)

}
