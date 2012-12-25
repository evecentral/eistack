import sbt._
import Keys._

object Build extends sbt.Build {
    lazy val root = Project(id = "eistack",
                            base = file(".")) aggregate(eithrift)

    lazy val eithrift = Project(id = "eithrift",
                           base = file("eithrift"))

}