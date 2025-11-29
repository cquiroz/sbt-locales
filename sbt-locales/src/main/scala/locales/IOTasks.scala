package locales

import java.io.InputStream
import java.io.{ File => JFile }
import java.net.URL
import sbt._
import sbt.io.{ IO => SbtIO }

object IOTasks {
  def downloadCLDR(
    coreZip:      JFile,
    log:          Logger,
    resourcesDir: JFile,
    cldrBaseUrl:  String,
    cldrVersion:  CLDRVersion
  ): Unit = {
    val localesDir = resourcesDir / "locales"
    val zipFile    = localesDir / coreZip.getName
    if (!zipFile.exists) {
      val url = s"$cldrBaseUrl/${cldrVersion.dir}/cldr-common-${cldrVersion.id}.zip"
      log.info(s"CLDR data missing. downloading ${cldrVersion.id} version to $localesDir...")
      log.info(s"downloading from $url")
      log.info(s"to file $coreZip")
      localesDir.mkdirs
      SbtIO.unzipURL(new URL(url), localesDir)
      log.info(s"CLDR files expanded on $localesDir")
    } else {
      log.debug("cldr files already available")
    }
  }

  def generateCLDR(
    base:    JFile,
    data:    JFile,
    filters: Filters
  ): Seq[JFile] =
    ScalaLocaleCodeGen.generateDataSourceCode(base, data, filters)

  def providerFile(base: JFile, name: String): File = base / name

  def copyProvider(log: Logger, base: JFile, name: String, packageDir: String): JFile = {
    val pathSeparator       = JFile.separator
    val packagePath         = packageDir.replaceAll("\\.", pathSeparator)
    val stream: InputStream = getClass.getResourceAsStream("/" + name)
    val destinationPath     = base / packagePath
    destinationPath.mkdirs()
    val destinationFile     = destinationPath / name
    SbtIO.delete(destinationFile)
    log.info(s"Copy $name to $destinationFile")
    SbtIO.transfer(stream, destinationFile)
    destinationFile
  }
}
