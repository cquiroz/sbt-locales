package locales.cldr

import java.util.Locale

final case class CLDRMetadata(
  isoCountries:  Array[String],
  iso3Countries: Map[String, String],
  isoLanguages:  Array[String],
  iso3Languages: Map[String, String],
  scripts:       Array[String]
)

/**
  * Interfaces describing an LDML Locale
  */
case class LDMLLocale(
  language:  String,
  territory: Option[String],
  variant:   Option[String],
  script:    Option[String]
)

/**
  * Wrapper to LDML
  */
case class LDML(
  parent:           Option[LDML],
  locale:           LDMLLocale,
  defaultNS:        Option[NumberingSystem],
  digitSymbols:     List[Symbols],
  calendarSymbols:  Option[CalendarSymbols],
  calendarPatterns: Option[CalendarPatterns],
  currencies:       List[NumberCurrency],
  numberPatterns:   NumberPatterns
) {

  private val byCurrencyCode: Map[String, NumberCurrency] =
    currencies.groupBy(_.currencyCode).map { case (code, list) => code.toUpperCase -> list.head }

  // Need to lookup the symbol & description independently
  def getNumberCurrencySymbol(currencyCode: String): Seq[CurrencySymbol] =
    byCurrencyCode
      .get(currencyCode.toUpperCase)
      .filter(_.symbols.nonEmpty)
      .map(_.symbols)
      .orElse(parent.map(_.getNumberCurrencySymbol(currencyCode)))
      .getOrElse(IndexedSeq.empty)

  def getNumberCurrencyDescription(currencyCode: String): Seq[CurrencyDisplayName] =
    byCurrencyCode
      .get(currencyCode.toUpperCase)
      .filter(_.displayNames.nonEmpty)
      .map(_.displayNames)
      .orElse(parent.map(_.getNumberCurrencyDescription(currencyCode)))
      .getOrElse(IndexedSeq.empty)

  def languageTag: String = toLocale.toLanguageTag

  def toLocale: Locale =
    if (locale.language == "root")
      new Locale.Builder()
        .setLanguage("")
        .setRegion(locale.territory.getOrElse(""))
        .setScript(locale.script.getOrElse(""))
        .setVariant(locale.variant.getOrElse(""))
        .build
    else
      new Locale.Builder()
        .setLanguage(locale.language)
        .setRegion(locale.territory.getOrElse(""))
        .setScript(locale.script.getOrElse(""))
        .setVariant(locale.variant.getOrElse(""))
        .build
}

final case class XMLLDML(
  locale:           LDMLLocale,
  fileName:         String,
  defaultNS:        Option[NumberingSystem],
  digitSymbols:     Map[NumberingSystem, NumberSymbols],
  calendar:         Option[CalendarSymbols],
  calendarPatterns: Option[CalendarPatterns],
  currencies:       Seq[NumberCurrency],
  numberPatterns:   NumberPatterns
) {

  val scalaSafeName: String =
    List(Some(locale.language), locale.script, locale.territory, locale.variant).flatten
      .mkString("_", "_", "")

}
