package locales.cldr

import java.util.Locale

final case class CLDRMetadata(isoCountries: Array[String], isoLanguages: Array[String], scripts: Array[String])

/** Interfaces describing calendar data */
case class Calendar(id: String) {
  def safeName: String = id.replace("-", "_")
}

case class CalendarSymbols(months: List[String], shortMonths: List[String],
    weekdays: List[String], shortWeekdays: List[String], amPm: List[String],
    eras: List[String])

/**
 * Interfaces describing the digit symbols
 */
case class NumberingSystem(id: String, digits: Seq[Char])

/** Number Formatting Patterns */
case class NumberPatterns(
    decimalFormat: Option[String],
    percentFormat: Option[String],
    currencyFormat: Option[String])

case class Symbols(ns: NumberingSystem, aliasOf: Option[NumberingSystem],
    decimal: Option[Char], group: Option[Char], list: Option[Char],
    percent: Option[Char], minus: Option[Char], perMille: Option[Char],
    infinity: Option[String], nan: Option[String], exp: Option[String])

/**
 * Interfaces describing an LDML Locale
 */
case class LDMLLocale(language: String, territory: Option[String],
    variant: Option[String], script: Option[String])

/**
 * Wrapper to LDML
 */
case class LDML(parent: Option[LDML],
    locale: LDMLLocale,
    defaultNS: Option[NumberingSystem],
    digitSymbols: List[Symbols] = Nil,
    calendarSymbols: Option[CalendarSymbols],
    calendarPatterns: Option[CalendarPatterns],
    currencies: List[NumberCurrency],
    numberPatterns: NumberPatterns) {

  private val byCurrencyCode: Map[String, NumberCurrency] =
    currencies.groupBy{ _.currencyCode }.map{ case (code, list) => code.toUpperCase -> list.head }

  // Need to lookup the symbol & description independently
  def getNumberCurrencySymbol(currencyCode: String): Seq[CurrencySymbol] = {
    (
      byCurrencyCode.get(currencyCode.toUpperCase).filter{ _.symbols.nonEmpty }.map{ _.symbols } orElse
      parent.map{ _.getNumberCurrencySymbol(currencyCode) }
    ).getOrElse(IndexedSeq.empty)
  }

  def getNumberCurrencyDescription(currencyCode: String): Seq[CurrencyDisplayName] = {
    (
      byCurrencyCode.get(currencyCode.toUpperCase).filter{ _.displayNames.nonEmpty }.map{ _.displayNames } orElse
      parent.map{ _.getNumberCurrencyDescription(currencyCode) }
    ).getOrElse(IndexedSeq.empty)
  }

  def languageTag: String = toLocale.toLanguageTag

  def toLocale: Locale = {
    if (locale.language == "root") {
      new Locale.Builder()
        .setLanguage("")
        .setRegion(locale.territory.getOrElse(""))
        .setScript(locale.script.getOrElse(""))
        .setVariant(locale.variant.getOrElse(""))
        .build
    } else {
      new Locale.Builder()
        .setLanguage(locale.language)
        .setRegion(locale.territory.getOrElse(""))
        .setScript(locale.script.getOrElse(""))
        .setVariant(locale.variant.getOrElse(""))
        .build
    }
  }
}
