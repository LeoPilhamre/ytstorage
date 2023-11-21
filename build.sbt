val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "ytstorage",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    resolvers += "Xuggle Repo" at "https://xuggle.googlecode.com/svn/trunk/repo/share/java/",

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "net.glxn" % "qrgen" % "1.4",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.32",
    libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.32",
    libraryDependencies += "xuggle" % "xuggle-xuggler" % "5.4" from "https://files.liferay.com/mirrors/xuggle.googlecode.com/svn/trunk/repo/share/java/xuggle/xuggle-xuggler/5.4/xuggle-xuggler-5.4.jar"
  )
