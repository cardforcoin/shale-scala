name := "shale"

version in ThisBuild := "0.0"

organization in ThisBuild := "com.cardforcoin"

scalaVersion in ThisBuild := "2.11.1"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-feature")

lazy val lib = project in file("shale-lib")

lazy val service = project in file("shale-service") dependsOn lib
