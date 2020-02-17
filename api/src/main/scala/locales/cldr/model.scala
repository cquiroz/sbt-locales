package locales.cldr

/**
  * Calendar value objects built from calendar CLDR XML data
  */
final case class MonthSymbols(months: List[String], shortMonths: List[String])
object MonthSymbols {
  val Zero = MonthSymbols(List.empty, List.empty)
}

final case class WeekdaysSymbols(weekdays: List[String], shortWeekdays: List[String])
object WeekdaysSymbols {
  val Zero = WeekdaysSymbols(List.empty, List.empty)
}

final case class AmPmSymbols(amPm: List[String])
object AmPmSymbols {
  val Zero = AmPmSymbols(List.empty)
}

final case class EraSymbols(eras: List[String])
object EraSymbols {
  val Zero = EraSymbols(List.empty)
}

final case class CalendarPatterns(
  datePatterns: Map[Int, String],
  timePatterns: Map[Int, String]
)

object CalendarPatterns {
  val Zero = CalendarPatterns(Map.empty, Map.empty)
}

/**
  * Calendar value objects built from calendar CLDR XML data
  */
final case class NumberSymbols(
  system:   NumberingSystem,
  aliasOf:  Option[NumberingSystem] = None,
  decimal:  Option[Char] = None,
  group:    Option[Char] = None,
  list:     Option[Char] = None,
  percent:  Option[Char] = None,
  plus:     Option[Char] = None,
  minus:    Option[Char] = None,
  perMille: Option[Char] = None,
  infinity: Option[String] = None,
  nan:      Option[String] = None,
  exp:      Option[String] = None
)

object NumberSymbols {
  def alias(system: NumberingSystem, aliasOf: NumberingSystem): NumberSymbols =
    NumberSymbols(system, aliasOf = Some(aliasOf))
}

// http://www.unicode.org/reports/tr35/tr35-numbers.html#Currencies
final case class CurrencyDisplayName(name: String, count: Option[String])
final case class CurrencySymbol(symbol:    String, alt:   Option[String])

final case class NumberCurrency(
  currencyCode: String,
  symbols:      Seq[CurrencySymbol],
  displayNames: Seq[CurrencyDisplayName]
)

// CurrencyData in supplemental/supplementalData.xml that defines currency availability by region
//    and digits/formatting, augment with Numeric Code Mappings & Master Currency Code List
final case class CurrencyData(
  currencyTypes: Seq[CurrencyType],
  fractions:     Seq[CurrencyDataFractionsInfo],
  regions:       Seq[CurrencyDataRegion],
  numericCodes:  Seq[CurrencyNumericCode]
)

final case class CurrencyType(currencyCode: String, currencyName: String)

final case class CurrencyNumericCode(currencyCode: String, numericCode: Int)

// currency code "DEFAULT" is used if currency code doesn't exist
final case class CurrencyDataFractionsInfo(
  currencyCode: String,
  digits:       Int,
  rounding:     Int,
  cashDigits:   Option[Int],
  cashRounding: Option[Int]
)

final case class CurrencyDataRegion(
  countryCode: String,
  currencies:  Seq[CurrencyDataRegionCurrency]
)

final case class CurrencyDataRegionCurrency(
  currencyCode: String,
  from:         Option[String],
  to:           Option[String],
  tender:       Option[Boolean]
)

final case class XMLLDMLLocale(
  language:  String,
  territory: Option[String],
  variant:   Option[String],
  script:    Option[String]
)

final case class XMLLDML(
  locale:           XMLLDMLLocale,
  fileName:         String,
  defaultNS:        Option[NumberingSystem],
  digitSymbols:     Map[NumberingSystem, NumberSymbols],
  calendar:         Option[CalendarSymbols],
  calendarPatterns: Option[CalendarPatterns],
  currencies:       Seq[NumberCurrency],
  numberPatterns:   NumberPatterns
) {

  val scalaSafeName: String = {
    List(Some(locale.language), locale.script, locale.territory, locale.variant).flatten
      .mkString("_")
  }
}
