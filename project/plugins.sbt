resolvers += "Local" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

addSbtPlugin("com.twitter" %% "sbt11-scrooge" % "3.0.1-SNAPSHOT")
