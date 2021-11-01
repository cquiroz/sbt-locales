import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }
import locales._

lazy val root =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .enablePlugins(LocalesPlugin)
    .in(file("."))
    .settings(
      name := "no-filtering",
      scalaVersion := "2.13.7",
      cldrVersion := CLDRVersion.Version("35.0"),
      localesFilter := LocalesFilter.All,
      nsFilter := NumberingSystemFilter.All,
      calendarFilter := CalendarFilter.All,
      libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.1.1"
    )
