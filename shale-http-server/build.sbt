name := "shale-http-server"

libraryDependencies += "org.http4s" %% "http4s-blazeserver" % "0.6.4"

libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.6.4"

libraryDependencies += "org.http4s" %% "http4s-argonaut" % "0.6.4"

// for scalaz-stream
resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies += "io.argonaut" %% "argonaut" % "6.1-M5"
