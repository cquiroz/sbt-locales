import sbt._
import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }

Global / onChangedBuildSource := ReloadOnSourceChanges

pluginCrossBuild / sbtVersion := "1.2.8"

inThisBuild(
  List(
    organization := "io.github.cquiroz",
    homepage := Some(url("https://github.com/cquiroz/sbt-locales")),
    licenses := Seq("BSD 3-Clause License" -> url("https://opensource.org/licenses/BSD-3-Clause")),
    developers := List(
      Developer("cquiroz",
                "Carlos Quiroz",
                "carlos.m.quiroz@gmail.com",
                url("https://github.com/cquiroz")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/cquiroz/sbt-locales"),
        "scm:git:git@github.com:cquiroz/sbt-locales.git"
      )
    )
  )
)

lazy val scalaVersion212 = "2.12.19" // needs to match the version for sbt

lazy val commonSettings = Seq(
  name := "sbt-locales",
  scalaVersion := scalaVersion212,
  javaOptions ++= Seq("-Dfile.encoding=UTF8"),
  autoAPIMappings := true
)

lazy val api = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("api"))
  .settings(commonSettings: _*)
  .settings(
    name := "cldr-api",
    scalaVersion := scalaVersion212,
    description := "scala-java-locales cldrl api",
    crossScalaVersions := Seq(scalaVersion212, "2.13.14", "3.3.3"),
    libraryDependencies ++= List(
      ("org.portable-scala" %%% "portable-scala-reflect" % "1.1.3").cross(CrossVersion.for3Use2_13),
      "org.scalameta"       %%% "munit"                  % "1.0.0" % Test
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
  .jsSettings(scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)))

lazy val sbt_locales = project
  .in(file("sbt-locales"))
  .enablePlugins(SbtPlugin)
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "sbt-locales",
    description := "Sbt plugin to build custom locale databases",
    scalaVersion := scalaVersion212,
    crossScalaVersions := Seq(),
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    Compile / resources ++= (api.jvm / Compile / sources).value,
    scriptedBufferLog := false,
    libraryDependencies ++= Seq(
      "com.eed3si9n"           %% "gigahorse-okhttp" % "0.7.0",
      "org.scala-lang.modules" %% "scala-xml"        % "2.3.0",
      "org.typelevel"          %% "cats-core"        % "2.10.0",
      "org.typelevel"          %% "cats-effect"      % "3.5.4",
      "com.eed3si9n"           %% "treehugger"       % "0.4.4"
    )
  )
  .dependsOn(api.jvm)

lazy val root = project
  .in(file("."))
  .settings(
    publish := {},
    publishLocal := {},
    publishArtifact := false
  )
  .aggregate(api.js, api.jvm, api.native, sbt_locales)
