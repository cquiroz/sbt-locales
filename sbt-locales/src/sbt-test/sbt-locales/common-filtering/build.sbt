import sbtcrossproject.CrossPlugin.autoImport.{ CrossType, crossProject }
import locales._

lazy val root =
  crossProject(JSPlatform, JVMPlatform) //, NativePlatform)
    .enablePlugins(LocalesPlugin)
    .in(file("."))
    .settings(
      name := "no-filtering",
      scalaVersion := "2.12.10",
      localesFilter := LocalesFilter.Selection("en_US", "fi", "fi_FI"),
      nsFilter := NumberingSystemFilter.Minimal,
      currencyFilter := CurrencyFilter.Selection("EUR"),
      supportISOCodes := true,
      supportNumberFormats := true,
      libraryDependencies += "org.portable-scala" %%% "portable-scala-reflect" % "1.0.0"
    )
