package locales

import java.io.{ File => JFile }
import better.files._
import sbt._
import sbt.util.Logger
import Keys._
import cats._
import cats.implicits._
import cats.effect
import org.scalajs.sbtplugin.ScalaJSPlugin

object LocalesPlugin extends AutoPlugin {
  sealed trait CLDRVersion {
    val id: String
  }
  case object LatestVersion extends CLDRVersion {
    val id: String = "latest"
  }
  final case class Version(version: String) extends CLDRVersion {
    val id: String = version
  }

  object autoImport {

    /**
      * Settings
      */
    val nsFilter               = settingKey[NumberingSystemFilter]("Filter for numbering systems")
    val calendarFilter         = settingKey[CalendarFilter]("Filter for calendars")
    val localesFilter          = settingKey[LocalesFilter]("Filter for locales")
    val currencyFilter         = settingKey[CurrencyFilter]("Filter for currencies")
    val currencyRegionFilter   = settingKey[CurrencyRegionFilter]("Filter for currency regions")
    val supportDateTimeFormats = settingKey[Boolean]("Include data to format dates and times")
    val supportNumberFormats   = settingKey[Boolean]("Include number formats")
    val dbVersion              = settingKey[CLDRVersion]("Version of the cldr database")
    val localesCodeGen =
      taskKey[Seq[JFile]]("Generate scala.js compatible database of tzdb data")
    lazy val baseLocalesSettings: Seq[Def.Setting[_]] =
      Seq(
        sourceGenerators in Compile += Def.task {
          localesCodeGen.value
        },
        localesCodeGen := Def.task {
          val cacheLocation    = streams.value.cacheDirectory / s"cldr-locales"
          val log              = streams.value.log
          val resourcesManaged = (resourceManaged in Compile).value
          val coreZip          = resourcesManaged / "core.zip"
          val cachedActionFunction: Set[JFile] => Set[JFile] =
            FileFunction.cached(
              cacheLocation,
              inStyle = FilesInfo.hash
            ) {
              _ =>
                log.info(s"Building cldr library")
                val filters = Filters(
                  localesFilter.value,
                  nsFilter.value,
                  calendarFilter.value,
                  currencyFilter.value,
                  currencyRegionFilter.value,
                  supportDateTimeFormats.value,
                  supportNumberFormats.value
                )
                localesCodeGenImpl(
                  sourceManaged    = (sourceManaged in Compile).value,
                  resourcesManaged = (resourceManaged in Compile).value,
                  filters,
                  dbVersion = dbVersion.value,
                  log       = log
                )
            }
          cachedActionFunction.apply(Set(coreZip)).toSeq
        }.value,
        libraryDependencies += "org.portable-scala" %% "portable-scala-reflect" % "1.0.0"
      )
  }

  import autoImport._
  override def trigger = noTrigger
  // override def requires = plugin.JSPlugin
  override def requires = org.scalajs.sbtplugin.ScalaJSPlugin
  // override def requires = ScalaJSPlugin // org.scalajs.sbtplugin.ScalaJSPlugin
  override lazy val buildSettings = Seq(
    localesFilter := LocalesFilter.Selection("root"),
    nsFilter := NumberingSystemFilter.Selection("latm"),
    calendarFilter := CalendarFilter.Selection("gregorian"),
    currencyFilter := CurrencyFilter.None,
    currencyRegionFilter := CurrencyRegionFilter.None,
    supportDateTimeFormats := true,
    supportNumberFormats := false,
    dbVersion := LatestVersion
  )
  // a group of settings that are automatically added to projects.
  override val projectSettings =
    inConfig(Compile)(baseLocalesSettings)

  def localesCodeGenImpl(
    sourceManaged:    JFile,
    resourcesManaged: JFile,
    filters:          Filters,
    dbVersion:        CLDRVersion,
    log:              Logger
  ): Set[JFile] =
    (for {
      _ <- IOTasks.downloadCLDR(log, resourcesManaged, dbVersion)
      // Use it to detect if files have been already generated
      f1 <- IOTasks.copyProvider(log, sourceManaged, "calendar.scala", "locales/cldr")
      f2 <- IOTasks.copyProvider(log, sourceManaged, "cldr.scala", "locales/cldr")
      f3 <- IOTasks.copyProvider(log, sourceManaged, "currency.scala", "locales/cldr")
      f4 <- IOTasks.copyProvider(log, sourceManaged, "number.scala", "locales/cldr")
      f5 <- IOTasks.copyProvider(log, sourceManaged, "package.scala", "locales/cldr")
      f6 <- IOTasks.copyProvider(log, sourceManaged, "provider.scala", "locales/cldr")
      f7 <- IOTasks.copyProvider(log, sourceManaged, "ldmlprovider.scala", "locales/cldr")
      f <- IOTasks.generateCLDR(
        sourceManaged,
        resourcesManaged / "locales",
        filters
      )
    } yield Seq(f1, f2, f3, f4, f5, f6, f7) ++ f).unsafeRunSync.toSet
}
