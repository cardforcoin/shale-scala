name := "shale-service"

crossPaths := false

libraryDependencies += "com.typesafe.akka" % "akka-slf4j_2.11" % "2.3.6"

libraryDependencies += "edu.gatech.gtri.typesafeconfig-extensions" %
  "typesafeconfig-factory" % "1.1"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
