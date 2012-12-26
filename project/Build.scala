import sbt._
import Keys._

object Build extends sbt.Build {
    lazy val root = Project(id = "eistack",
                            base = file(".")) aggregate(eiprotobuf)

    lazy val eiprotobuf = Project(id = "eiprotobuf",
                           base = file("eiprotobuf"))

}