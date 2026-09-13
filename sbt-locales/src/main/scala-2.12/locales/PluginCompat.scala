package locales

import java.nio.file.{ Path => NioPath }
import sbt._

private[locales] object PluginCompat {
  type FileRef = java.io.File
  type Out     = java.io.File

  def toNioPath(a: Attributed[java.io.File]): NioPath      = a.data.toPath
  def toFile(a:    Attributed[java.io.File]): java.io.File = a.data
}
