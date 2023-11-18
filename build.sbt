val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "ytstorage",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    resolvers += "QRGen" at "http://kenglxn.github.com/QRGen/repository",

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "net.glxn" % "qrgen" % "1.1"
  )
