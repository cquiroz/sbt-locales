package locales.cldr.data.model

import locales.cldr.Calendar
import locales.cldr.NumberPatterns
import locales.cldr.NumberingSystem

trait ScalaSafeName[T] {
  def scalaSafeName(t: T): String
}

object ScalaSafeName {
  def apply[A](implicit ev: ScalaSafeName[A]): ScalaSafeName[A] = ev

  implicit val CalendarSafeName: ScalaSafeName[Calendar] = new ScalaSafeName[Calendar] {
    override def scalaSafeName(c: Calendar): String = c.id.replace("-", "_")
  }

  implicit class ScalaSafeNameOps[T: ScalaSafeName](s: T) {
    def scalaSafeName: String = ScalaSafeName[T].scalaSafeName(s)
  }
}

/**
  * Calendar value objects built from calendar CLDR XML data
  */
final case class MonthSymbols(months: Seq[String], shortMonths: Seq[String])
object MonthSymbols {
  val Zero = MonthSymbols(Seq.empty, Seq.empty)
}

final case class WeekdaysSymbols(weekdays: Seq[String], shortWeekdays: Seq[String])
object WeekdaysSymbols {
  val Zero = WeekdaysSymbols(Seq.empty, Seq.empty)
}

final case class AmPmSymbols(amPm: Seq[String])
object AmPmSymbols {
  val Zero = AmPmSymbols(Seq.empty)
}

final case class EraSymbols(eras: Seq[String])
object EraSymbols {
  val Zero = EraSymbols(Seq.empty)
}

final case class CalendarSymbols(months: MonthSymbols, weekdays: WeekdaysSymbols,
    amPm: AmPmSymbols, eras: EraSymbols)

final case class DateTimePattern(patternType: String, pattern: String)

final case class CalendarPatterns(datePatterns: List[DateTimePattern], timePatterns: List[DateTimePattern])

object CalendarPatterns {
  val Zero = CalendarPatterns(Nil, Nil)
}

/**
  * Calendar value objects built from calendar CLDR XML data
  */

final case class NumberSymbols(system: NumberingSystem,
    aliasOf: Option[NumberingSystem] = None,
    decimal: Option[Char] = None,
    group: Option[Char] = None,
    list: Option[Char] = None,
    percent: Option[Char] = None,
    plus: Option[Char] = None,
    minus: Option[Char] = None,
    perMille: Option[Char] = None,
    infinity: Option[String] = None,
    nan: Option[String] = None,
    exp: Option[String] = None)

object NumberSymbols {
  def alias(system: NumberingSystem, aliasOf: NumberingSystem): NumberSymbols =
    NumberSymbols(system, aliasOf = Some(aliasOf))
}

// http://www.unicode.org/reports/tr35/tr35-numbers.html#Currencies
final case class CurrencyDisplayName(name: String, count: Option[String])
final case class CurrencySymbol(symbol: String, alt: Option[String])

final case class NumberCurrency(currencyCode: String,
    symbols: Seq[CurrencySymbol],
    displayNames: Seq[CurrencyDisplayName])

// CurrencyData in supplemental/supplementalData.xml that defines currency availability by region
//    and digits/formatting, augment with Numeric Code Mappings & Master Currency Code List
final case class CurrencyData(currencyTypes: Seq[CurrencyType],
    fractions: Seq[CurrencyDataFractionsInfo],
    regions: Seq[CurrencyDataRegion],
    numericCodes: Seq[CurrencyNumericCode])

final case class CurrencyType(currencyCode: String, currencyName: String)

final case class CurrencyNumericCode(currencyCode: String, numericCode: Int)

final case class CurrencyDataFractionsInfo(currencyCode: String, digits: Int, rounding: Int,
    cashDigits: Option[Int], cashRounding: Option[Int])

final case class CurrencyDataRegion(countryCode: String, currencies: Seq[CurrencyDataRegionCurrency])

final case class CurrencyDataRegionCurrency(currencyCode: String,
    from: Option[String], to: Option[String], tender: Option[Boolean])

final case class XMLLDMLLocale(language: String, territory: Option[String],
    variant: Option[String], script: Option[String])

final case class XMLLDML(locale: XMLLDMLLocale, fileName: String, defaultNS: Option[NumberingSystem],
    digitSymbols: Map[NumberingSystem, NumberSymbols], calendar: Option[CalendarSymbols],
    datePatterns: Option[CalendarPatterns],
    currencies: Seq[NumberCurrency],
    numberPatterns: NumberPatterns) {

  val scalaSafeName: String = {
    List(Some(locale.language), locale.script, locale.territory, locale.variant)
      .flatten.mkString("_")
  }
}
