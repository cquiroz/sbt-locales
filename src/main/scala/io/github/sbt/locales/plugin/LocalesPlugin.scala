package io.github.cquiroz.sbt.locales.plugin

import java.io.{File => JFile}
import better.files._
import sbt._
import sbt.util.Logger
import Keys._
import cats._
import cats.implicits._
import cats.effect
import org.scalajs.sbtplugin

object LocalesPlugin extends AutoPlugin {
  sealed trait CLDRVersion {
    val id: String
    val path: String
  }
  case object LatestVersion extends CLDRVersion {
    val id: String   = "latest"
    val path: String = "tzdata-latest"
  }
  final case class Version(version: String) extends CLDRVersion {
    val id: String   = version
    val path: String = s"releases/tzdata$version"
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
  override def requires = sbtplugin.ScalaJSPlugin
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
      localesCodeGen :=
        localesCodeGenImpl(
          sourceManaged = (sourceManaged in Compile).value,
          resourcesManaged = (resourceManaged in Compile).value,
          localesFilter = localesFilter.value,
          dbVersion = dbVersion.value,
          log = streams.value.log
        )
    )

  def localesCodeGenImpl(sourceManaged: JFile,
                      resourcesManaged: JFile,
                      localesFilter: String => Boolean,
                      dbVersion: CLDRVersion,
                      log: Logger): Seq[JFile] = {
    // val tzdbData: JFile = resourcesManaged / "tzdb"
    // val ttbp = IOTasks.copyProvider(sourceManaged,
    //                                 "TzdbZoneRulesProvider.scala",
    //                                 "org.threeten.bp.zone",
    //                                 false)
    // val jt =
    //   IOTasks.copyProvider(sourceManaged, "TzdbZoneRulesProvider.scala", "java.time.zone", true)
    // val providerCopy = if (includeTTBP) List(ttbp, jt) else List(jt)
    val r = (for {
      _ <- IOTasks.downloadCLDR(log, resourcesManaged, dbVersion)
      // Use it to detect if files have been already generated
      // p <- IOTasks.providerFile(sourceManaged, "TzdbZoneRulesProvider.scala", "java.time.zone")
      // e <- effect.IO(p.exists)
      // j <- if (e) effect.IO(List(p)) else providerCopy.sequence
      // f <- if (e) IOTasks.tzDataSources(sourceManaged, includeTTBP).map(_.map(_._3))
      _ <- IOTasks.generateCLDR(sourceManaged, resourcesManaged)

      //     else
      //       IOTasks.generateTZDataSources(sourceManaged, tzdbData, log, includeTTBP, zonesFilter)
    } yield ()).unsafeRunSync
    Seq.empty
  }
}
