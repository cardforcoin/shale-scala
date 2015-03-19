name := "shale"

version in ThisBuild := "0.0"

organization in ThisBuild := "com.cardforcoin"

scalaVersion in ThisBuild := "2.11.1"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-feature")

lazy val `redis-client` = project in file("redis-client")

lazy val `shale-lib` = project in file("shale-lib") dependsOn `redis-client`

lazy val `shale-service` = project in file("shale-service") dependsOn `shale-lib`
