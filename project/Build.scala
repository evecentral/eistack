import sbt._
import Keys._

object Build extends sbt.Build {
  lazy val root = Project(id = "eistack",
    base = file(".")) aggregate(eicore, eiprotobuf, eiingest)

  lazy val eiprotobuf = Project(id = "eiprotobuf",
    base = file("eiprotobuf"))
  lazy val eicore = Project(id = "eikillmailcore",
    base = file("eikillmailcore")).dependsOn(eiprotobuf)
  lazy val eiingest = Project(id = "ei_ingestserver",
    base = file("ei_ingestserver")).dependsOn(eicore)

}
