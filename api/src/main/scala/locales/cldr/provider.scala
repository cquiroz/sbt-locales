package locales.cldr

trait LocalesProvider {
  def root: LDML
  def ldmls: Map[String, LDML]
  def metadata: CLDRMetadata
  def latn: NumberingSystem
  def currencyData: CurrencyData
}
