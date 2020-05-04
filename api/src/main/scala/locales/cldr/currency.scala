package locales.cldr

// http://www.unicode.org/reports/tr35/tr35-numbers.html#Currencies
final case class CurrencyDisplayName(name: String, count: Option[String])
final case class CurrencySymbol(symbol: String, alt: Option[String])

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
