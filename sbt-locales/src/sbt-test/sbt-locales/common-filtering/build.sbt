import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }
import locales._

lazy val root =
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .enablePlugins(LocalesPlugin)
    .in(file("."))
    .settings(
      name := "no-filtering",
      cldrVersion := CLDRVersion.Version("38.1"),
      scalaVersion := "3.3.8",
      localesFilter := LocalesFilter.Selection("en-US", "fi", "fi-FI"),
      nsFilter := NumberingSystemFilter.Minimal,
      currencyFilter := CurrencyFilter.Selection("EUR"),
      supportISOCodes := true,
      supportNumberFormats := true
    )
    .jvmSettings(
      libraryDependencies += ("org.portable-scala" %% "portable-scala-reflect" % "1.1.3")
        .cross(CrossVersion.for3Use2_13)
    )
    .jsSettings(
      libraryDependencies += "org.portable-scala" % "portable-scala-reflect_sjs1_2.13" % "1.1.3"
    )
    .nativeSettings(
      libraryDependencies += "org.portable-scala" % "portable-scala-reflect_native0.5_2.13" % "1.1.3"
    )
