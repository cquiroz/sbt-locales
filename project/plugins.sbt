import sbt.Keys._

libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.3.2")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.20.1")

addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.5")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.4")

addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.6.1")
addSbtPlugin("org.typelevel"  % "sbt-tpolecat"   % "0.5.2")
