import sbt.Keys._

libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.0.0")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.4.0")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.0-test1")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")

addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.5.5")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.16")
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.5.1")
