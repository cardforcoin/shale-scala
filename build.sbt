name := "shale"

version in ThisBuild := "0.0"

organization in ThisBuild := "com.cardforcoin"

scalaVersion in ThisBuild := "2.11.1"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-feature")

lazy val `interval` = project in file("interval")

lazy val `redis-client` = project in file("redis-client")

lazy val `http-client` = project in file("http-client")

lazy val `logback-setup` = project in file("logback-setup")

lazy val `selenium-client` = project in file("selenium-client") dependsOn `http-client`

lazy val `shale-lib` = project in file("shale-lib") dependsOn (`redis-client`, `selenium-client`)

lazy val `shale-http-server` = project in file("shale-http-server") dependsOn `shale-lib`

lazy val `shale-service` = project in file("shale-service") dependsOn (`shale-http-server`, `logback-setup`)
