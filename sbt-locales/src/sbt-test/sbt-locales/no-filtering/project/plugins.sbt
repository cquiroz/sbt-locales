addSbtPlugin(
  "io.github.cquiroz" % "sbt-locales" % sys.props
    .getOrElse("plugin.version", sys.error("'plugin.version' environment variable is not set"))
)
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.0.1")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.11")
