import sbt.Keys._

libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.0.0")

val scalaJSVersion =
  Option(System.getenv("SCALAJS_VERSION")).getOrElse("1.0.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % scalaJSVersion)
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.3.9")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.0")
addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.5.0")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.10")
