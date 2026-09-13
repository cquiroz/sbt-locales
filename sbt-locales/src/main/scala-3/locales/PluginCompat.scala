package locales

import java.nio.file.{Path => NioPath}
import sbt.*
import xsbti.{FileConverter, HashedVirtualFileRef}

private[locales] object PluginCompat:
  type FileRef = HashedVirtualFileRef
  type Out = xsbti.VirtualFile

  def toNioPath(a: Attributed[HashedVirtualFileRef])(using conv: FileConverter): NioPath =
    conv.toPath(a.data)

  def toFile(a: Attributed[HashedVirtualFileRef])(using conv: FileConverter): java.io.File =
    conv.toPath(a.data).toFile
