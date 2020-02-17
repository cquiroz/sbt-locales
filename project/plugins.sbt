import sbt.Keys._

libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.0.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.32")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.3.9")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.0")
addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.5.0")
