package io.github.cquiroz.sbt.locales.plugin

import java.io.{File => JFile}
import better.files._
import sbt._
import sbt.util.Logger
import Keys._
import cats._
import cats.implicits._
import cats.effect

object LocalesPlugin extends AutoPlugin {
  sealed trait CLDRVersion {
    val id: String
  }
  case object LatestVersion extends CLDRVersion {
    val id: String   = "latest"
  }
  final case class Version(version: String) extends CLDRVersion {
    val id: String   = version
  }

  object autoImport {

    /*
     * Settings
     */
    val localesFilter = settingKey[String => Boolean]("Filter for locales names")
    val dbVersion   = settingKey[CLDRVersion]("Version of the cldr database")
    val localesCodeGen =
      taskKey[Seq[JFile]]("Generate scala.js compatible database of tzdb data")
  }

  import autoImport._
  override def trigger  = noTrigger
  override lazy val buildSettings = Seq(
    localesFilter := { case _ => true },
    dbVersion := LatestVersion
  )
  override val projectSettings =
    Seq(
      sourceGenerators in Compile += Def.task {
        localesCodeGen.value
      },
      localesCodeGen := Def.task {
        val cacheLocation = streams.value.cacheDirectory / s"cldr-locales"
        val log = streams.value.log
        val resourcesManaged = (resourceManaged in Compile).value
        val coreZip    = resourcesManaged / "core.zip"
        val cachedActionFunction: Set[JFile] => Set[JFile] =
          FileFunction.cached(
            cacheLocation,
            inStyle = FilesInfo.hash
          ) { _ =>
            log.info(
              s"Building cldr library")
            localesCodeGenImpl(
              sourceManaged = (sourceManaged in Compile).value,
              resourcesManaged = (resourceManaged in Compile).value,
              localesFilter = localesFilter.value,
              dbVersion = dbVersion.value,
              log = log
            )
          }
        cachedActionFunction.apply(Set(coreZip)).toSeq
      }.value
    )

  def localesCodeGenImpl(sourceManaged: JFile,
                      resourcesManaged: JFile,
                      localesFilter: String => Boolean,
                      dbVersion: CLDRVersion,
                      log: Logger): Set[JFile] = {
    // val tzdbData: JFile = resourcesManaged / "tzdb"
    // val ttbp = IOTasks.copyProvider(sourceManaged,
    //                                 "TzdbZoneRulesProvider.scala",
    //                                 "org.threeten.bp.zone",
    //                                 false)
    // val jt =
    //   IOTasks.copyProvider(sourceManaged, "TzdbZoneRulesProvider.scala", "java.time.zone", true)
    // val providerCopy = if (includeTTBP) List(ttbp, jt) else List(jt)
    (for {
      _ <- IOTasks.downloadCLDR(log, resourcesManaged, dbVersion)
      // Use it to detect if files have been already generated
      m <- IOTasks.copyProvider(sourceManaged, "model.scala", "locales/cldr")
      c <- IOTasks.copyProvider(sourceManaged, "cldr.scala", "locales/cldr")
      // e <- effect.IO(p.exists)
      // j <- if (e) effect.IO(List(p)) else providerCopy.sequence
      // f <- if (e) IOTasks.tzDataSources(sourceManaged, includeTTBP).map(_.map(_._3))
      f <- IOTasks.generateCLDR(sourceManaged, resourcesManaged / "locales", localesFilter)

      //     else
      //       IOTasks.generateTZDataSources(sourceManaged, tzdbData, log, includeTTBP, zonesFilter)
    } yield Seq(m, c) ++ f).unsafeRunSync.toSet
  }
}
