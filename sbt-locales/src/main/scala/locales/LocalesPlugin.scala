package locales

import java.io.{ File => JFile }
import sbt._
import sbt.util.Logger
import cats.effect.unsafe.implicits.global
import Keys._

object LocalesPlugin extends AutoPlugin {
  object autoImport {

    /**
      * Settings
      */
    val nsFilter                                      = settingKey[NumberingSystemFilter]("Filter for numbering systems")
    val calendarFilter                                = settingKey[CalendarFilter]("Filter for calendars")
    val localesFilter                                 = settingKey[LocalesFilter]("Filter for locales")
    val currencyFilter                                = settingKey[CurrencyFilter]("Filter for currencies")
    val supportDateTimeFormats                        = settingKey[Boolean]("Include data to format dates and times")
    val supportNumberFormats                          = settingKey[Boolean]("Include number formats")
    val supportISOCodes                               = settingKey[Boolean]("Include iso codes metadata")
    val cldrBaseUrl                                   = settingKey[String]("A base URL of cldr database")
    val cldrVersion                                   = settingKey[CLDRVersion]("Version of the cldr database")
    val localesCodeGen                                =
      taskKey[Seq[JFile]]("Generate scala.js compatible database of tzdb data")
    lazy val baseLocalesSettings: Seq[Def.Setting[_]] =
      Seq(
        Compile / sourceGenerators += Def.task {
          localesCodeGen.value
        },
        localesCodeGen := Def.task {
          val cacheLocation                                  = streams.value.cacheDirectory / s"cldr-locales"
          val log                                            = streams.value.log
          val coreZip                                        = cacheLocation / s"core-${cldrVersion.value}.zip"
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
                  supportDateTimeFormats.value,
                  supportNumberFormats.value,
                  supportISOCodes.value
                )
                localesCodeGenImpl(
                  coreZip,
                  sourceManaged = (Compile / sourceManaged).value,
                  resourcesManaged = (Compile / resourceManaged).value,
                  filters,
                  cldrBaseUrl = cldrBaseUrl.value,
                  cldrVersion = cldrVersion.value,
                  log = log
                )
            }
          cachedActionFunction.apply(Set(coreZip)).toSeq
        }.value,
        libraryDependencies += "org.portable-scala" %% "portable-scala-reflect" % "1.1.3"
      )
  }

  import autoImport._
  override def trigger            = noTrigger
  override lazy val buildSettings = Seq(
    localesFilter := LocalesFilter.Minimal,
    nsFilter := NumberingSystemFilter.Minimal,
    calendarFilter := CalendarFilter.Minimal,
    currencyFilter := CurrencyFilter.None,
    supportDateTimeFormats := true,
    supportNumberFormats := false,
    supportISOCodes := false,
    cldrBaseUrl := "http://unicode.org/Public/cldr",
    cldrVersion := CLDRVersion.Version("38.1")
  )
  // a group of settings that are automatically added to projects.
  override val projectSettings    =
    inConfig(Compile)(baseLocalesSettings)

  def localesCodeGenImpl(
    coreZip:          JFile,
    sourceManaged:    JFile,
    resourcesManaged: JFile,
    filters:          Filters,
    cldrBaseUrl:      String,
    cldrVersion:      CLDRVersion,
    log:              Logger
  ): Set[JFile] =
    (for {
      _  <- IOTasks.downloadCLDR(coreZip, log, resourcesManaged, cldrBaseUrl, cldrVersion)
      // Use it to detect if files have been already generated
      f1 <- IOTasks.copyProvider(log, sourceManaged, "calendar.scala", "locales/cldr")
      f2 <- IOTasks.copyProvider(log, sourceManaged, "cldr.scala", "locales/cldr")
      f3 <- IOTasks.copyProvider(log, sourceManaged, "currency.scala", "locales/cldr")
      f4 <- IOTasks.copyProvider(log, sourceManaged, "number.scala", "locales/cldr")
      f5 <- IOTasks.copyProvider(log, sourceManaged, "package.scala", "locales/cldr")
      f6 <- IOTasks.copyProvider(log, sourceManaged, "provider.scala", "locales/cldr")
      f7 <- IOTasks.copyProvider(log, sourceManaged, "ldmlprovider.scala", "locales/cldr")
      f  <- IOTasks.generateCLDR(
             sourceManaged,
             resourcesManaged / "locales",
             filters
           )
    } yield Seq(f1, f2, f3, f4, f5, f6, f7) ++ f).unsafeRunSync.toSet
}
