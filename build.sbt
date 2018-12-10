import sbt._
import sbt.io.Using

val scalaVer = "2.12.6"

lazy val commonSettings = Seq(
  name         := "sbt-locales",
  description  := "Sbt plugin to build custom locale databases",
  // version      := "0.0.1",
  organization := "io.github.cquiroz",
  homepage     := Some(url("https://github.com/cquiroz/sbt-locales")),
  licenses     := Seq("BSD 3-Clause License" -> url("https://opensource.org/licenses/BSD-3-Clause")),

  scalaVersion       := scalaVer,

  javaOptions ++= Seq("-Dfile.encoding=UTF8"),
  autoAPIMappings := true,
  useGpg := true,

  publishArtifact in Test := false,
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := pomData,
  pomIncludeRepository := { _ => false },
)

lazy val api = project
  .in(file("api"))
  .settings(commonSettings: _*)
  .settings(
    name := "cldr-api"
  )

lazy val sbt_locales = project
  .in(file("sbt-locales"))
  .settings(commonSettings: _*)
  .settings(
    name := "sbt-locales",
    sbtPlugin := true,
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    libraryDependencies ++= Seq(
      "com.eed3si9n"           %% "gigahorse-okhttp"     % "0.3.1",
      "org.scala-lang.modules" %% "scala-xml"            % "1.1.0",
      "com.github.pathikrit"   %% "better-files"         % "3.4.0",
      "org.typelevel"          %% "cats-core"            % "1.1.0",
      "org.typelevel"          %% "cats-effect"          % "0.10.1",
      "com.eed3si9n"           %% "treehugger"           % "0.4.3",
      "org.scalatest"          %% "scalatest"            % "3.0.4" % "test"
    )
  )

lazy val pomData =
  <scm>
    <url>git@github.com:cquiroz/sbt-locales.git</url>
    <connection>scm:git:git@github.com:cquiroz/sbt-locales.git</connection>
  </scm>
  <developers>
    <developer>
      <id>cquiroz</id>
      <name>Carlos Quiroz</name>
      <url>https://github.com/cquiroz</url>
      <roles>
        <role>Project Lead</role>
      </roles>
    </developer>
  </developers>
