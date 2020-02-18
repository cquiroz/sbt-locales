package locales.cldr

/**
  * Interfaces describing the digit symbols
  */
case class NumberingSystem(id: String, digits: Seq[Char])

/** Number Formatting Patterns */
case class NumberPatterns(
  decimalFormat:  Option[String],
  percentFormat:  Option[String],
  currencyFormat: Option[String]
)

case class Symbols(
  ns:       NumberingSystem,
  aliasOf:  Option[NumberingSystem],
  decimal:  Option[Char],
  group:    Option[Char],
  list:     Option[Char],
  percent:  Option[Char],
  minus:    Option[Char],
  perMille: Option[Char],
  infinity: Option[String],
  nan:      Option[String],
  exp:      Option[String]
)

/**
  * Symbols of a numeric system
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
