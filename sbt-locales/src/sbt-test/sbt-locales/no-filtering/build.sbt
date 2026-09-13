import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }
import locales._

lazy val root =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .enablePlugins(LocalesPlugin)
    .in(file("."))
    .settings(
      name := "no-filtering",
      scalaVersion := "2.13.18",
      cldrVersion := CLDRVersion.Version("35.0"),
      localesFilter := LocalesFilter.All,
      nsFilter := NumberingSystemFilter.All,
      calendarFilter := CalendarFilter.All
    )
    .jvmSettings(
      libraryDependencies += "org.portable-scala" %% "portable-scala-reflect" % "1.1.3"
    )
    .jsSettings(
      libraryDependencies += "org.portable-scala" % "portable-scala-reflect_sjs1_2.13" % "1.1.3"
    )
    .nativeSettings(
      libraryDependencies += "org.portable-scala" % "portable-scala-reflect_native0.5_2.13" % "1.1.3"
    )
