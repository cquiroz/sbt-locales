package locales.cldr

final case class LocalesProvider(latn: NumberingSystem)

object LocalesProvider {
  private var provider: LocalesProvider = null
  def registerLocalesProvider(p: LocalesProvider): Unit =
    this.provider = p
  def root: LDML = ???
  def latn: NumberingSystem = provider.latn
  def currencydata: CurrencyData = ???
  def metadata: CLDRMetadata = ???
}
