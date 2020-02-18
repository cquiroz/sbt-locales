import sbt._
import sbt.io.Using
import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }

val scalaVer = "2.12.10"

inThisBuild(
  List(
    organization := "io.github.cquiroz",
    homepage := Some(url("https://github.com/cquiroz/sbt-locales")),
    licenses := Seq("BSD 3-Clause License" -> url("https://opensource.org/licenses/BSD-3-Clause")),
      developers := List(
        Developer("cquiroz",
                "Carlos Quiroz",
                "carlos.m.quiroz@gmail.com",
                url("https://github.com/cquiroz"))
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/cquiroz/sbt-locales"),
        "scm:git:git@github.com:cquiroz/sbt-locales.git"
      )
    )
  )
)

lazy val commonSettings = Seq(
  name := "sbt-locales",
  description := "Sbt plugin to build custom locale databases",
  // version      := "0.0.1",
  scalaVersion := scalaVer,
  javaOptions ++= Seq("-Dfile.encoding=UTF8"),
  autoAPIMappings := true
)

lazy val api = crossProject(JSPlatform, JVMPlatform) //, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("api"))
  .settings(commonSettings: _*)
  .settings(
    name := "cldr-api",
    scalaVersion := "2.12.10",
    crossScalaVersions := {
      if (scalaJSVersion.startsWith("0.6")) {
        Seq("2.10.7", "2.11.12", "2.12.10", "2.13.1")
      } else {
        Seq("2.11.12", "2.12.10", "2.13.1")
      }
    },
    libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.0.0"
  )

lazy val sbt_locales = project
  .in(file("sbt-locales"))
  .enablePlugins(SbtPlugin)
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "sbt-locales",
    scalaVersion := "2.12.10",
    crossScalaVersions := Seq(),
    publishArtifact in (Compile, packageDoc) := false,
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    resources in Compile ++= (sources in (api.jvm, Compile)).value,
    scriptedBufferLog := false,
    addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.32"),
    addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0"),
    libraryDependencies ++= Seq(
      "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
      "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
      "com.github.pathikrit" %% "better-files" % "3.8.0",
      "org.typelevel" %% "cats-core" % "2.1.0",
      "org.typelevel" %% "cats-effect" % "2.1.0",
      "com.eed3si9n" %% "treehugger" % "0.4.4"
    )
  )
  .dependsOn(api.jvm)
