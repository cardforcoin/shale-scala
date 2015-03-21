name := "shale-lib"

libraryDependencies ++= Seq("akka-actor")
  .map(a => "com.typesafe.akka" % (a + "_2.11") % "2.3.6")

libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.42.2"

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= Seq("spray-can", "spray-routing", "spray-client")
  .map(a => "io.spray" % (a + "_2.11") % "1.3.1")

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

libraryDependencies += "com.github.nscala-time" % "nscala-time_2.11" % "1.4.0"

libraryDependencies += "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0"
