package locales.cldr.data

import locales.cldr._
import org.portablescala.reflect.annotation.EnableReflectiveInstantiation

@EnableReflectiveInstantiation
object LocalesProvider extends LocalesProvider {
  def root: LDML               = data._root
  def ldmls: Map[String, LDML] = Map.empty
  def latn: NumberingSystem    = data.numericsystems.latn
  def currencyData: CurrencyData =
    new CurrencyData(
      data.currencydata.currencyTypes,
      data.currencydata.fractions,
      data.currencydata.regions,
      data.currencydata.numericCodes
    )
  def metadata: CLDRMetadata =
    new CLDRMetadata(data.metadata.isoCountries, data.metadata.isoLanguages, data.metadata.scripts)
}
