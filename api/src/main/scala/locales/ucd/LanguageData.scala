package locales.ucd

final case class LanguageData(
  language: String,
  scripts: Seq[String],
  territories: Seq[String],
  isSecondary: Boolean
)