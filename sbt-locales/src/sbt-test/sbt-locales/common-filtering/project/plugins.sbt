addSbtPlugin(
  "io.github.cquiroz"                    % "sbt-locales"                   % sys.props
    .getOrElse("plugin.version", sys.error("'plugin.version' environment variable is not set"))
)
addSbtPlugin("org.portable-scala"        % "sbt-scalajs-crossproject"      % "1.2.0")
addSbtPlugin("org.scala-js"              % "sbt-scalajs"                   % "1.13.2")
addSbtPlugin("org.portable-scala"        % "sbt-scala-native-crossproject" % "1.2.0")
addSbtPlugin("org.scala-native"          % "sbt-scala-native"              % "0.4.3")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"                  % "0.3.1")
addSbtPlugin("ch.epfl.lamp"              % "sbt-dotty"                     % "0.5.5")
