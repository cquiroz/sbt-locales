import sbt.Keys._

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.2")

libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value
