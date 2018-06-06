addSbtPlugin("io.github.cquiroz" % "sbt-locales" % sys.props.getOrElse("plugin.version", sys.error("'plugin.version' environment variable is not set")))
