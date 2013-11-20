resolvers += "gseitz@github" at "http://gseitz.github.com/maven/"

addSbtPlugin("com.github.gseitz" % "sbt-protobuf" % "0.3.0")

resolvers += "Local" at "file://"+Path.userHome.absolutePath+"/.m2/repository"


