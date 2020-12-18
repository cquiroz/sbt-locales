import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }
import locales._

lazy val root =
  crossProject(JSPlatform, JVMPlatform) //, NativePlatform)
    .enablePlugins(LocalesPlugin)
    .in(file("."))
    .settings(
      name := "no-filtering",
      cldrVersion := CLDRVersion.Version("38.1"),
      scalaVersion := "3.0.0-M3",
      localesFilter := LocalesFilter.Selection("en-US", "fi", "fi-FI"),
      nsFilter := NumberingSystemFilter.Minimal,
      currencyFilter := CurrencyFilter.Selection("EUR"),
      supportISOCodes := true,
      supportNumberFormats := true,
      libraryDependencies += ("org.portable-scala" %%% "portable-scala-reflect" % "1.0.0").withDottyCompat(scalaVersion.value)
    )
