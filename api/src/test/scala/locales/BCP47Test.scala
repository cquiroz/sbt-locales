package locales

import locales.BCP47._

class BCP47Test extends munit.FunSuite {

  test("test_grandfathered") {
    val regularGrandFathered   = BCP47.regular.split("\\|")
    val irregularGrandFathered = BCP47.irregular.split("\\|")
    val grandFathered          = regularGrandFathered ++ irregularGrandFathered
    grandFathered.map(BCP47.parseTag).zip(grandFathered).foreach {
      case (Some(GrandfatheredTag(t)), g) => assertEquals(g, t)
      case _                              => fail("Shouldn't happen")
    }
  }

  test("test_ext_lang") {
    val chineseWithExt = List("zh-gan", "zh-yue", "zh-cmn")
    chineseWithExt.map(BCP47.parseTag).zip(chineseWithExt).foreach {
      case (Some(LanguageTag(lang, extLang, _, _, _, _, _)), t) =>
        assertEquals("zh", lang)
        assertEquals(Option(t.replace("zh-", "")), extLang)

      case _ => fail("Shouldn't happen")
    }
  }

  // samples taken from Appendix A of the BCP 47 specification
  // https://tools.ietf.org/html/bcp47#appendix-A
  test("test_simple_languages_subtag_samples") {
    // Simple language subtag:
    // de (German)
    assertEquals(Option[BCP47Tag](LanguageTag("de", None, None, None, Nil, Nil, None)),
                 BCP47.parseTag("de")
    )
    // fr (French)
    assertEquals(Option[BCP47Tag](LanguageTag("fr", None, None, None, Nil, Nil, None)),
                 BCP47.parseTag("fr")
    )
    // ja (Japanese)
    assertEquals(Option[BCP47Tag](LanguageTag("ja", None, None, None, Nil, Nil, None)),
                 BCP47.parseTag("ja")
    )
    // i-enochian (example of a grandfathered tag)
    assertEquals(Option[BCP47Tag](GrandfatheredTag("i-enochian")), BCP47.parseTag("i-enochian"))
  }

  test("test_languages_script_samples") {
    // Language subtag plus Script subtag:
    // zh-Hant (Chinese written using the Traditional Chinese script)
    assertEquals(Option[BCP47Tag](LanguageTag("zh", None, Some("Hant"), None, Nil, Nil, None)),
                 BCP47.parseTag("zh-Hant")
    )
    // zh-Hans (Chinese written using the Simplified Chinese script)
    assertEquals(Option[BCP47Tag](LanguageTag("zh", None, Some("Hans"), None, Nil, Nil, None)),
                 BCP47.parseTag("zh-Hans")
    )
    // sr-Cyrl (Serbian written using the Cyrillic script)
    assertEquals(Option[BCP47Tag](LanguageTag("sr", None, Some("Cyrl"), None, Nil, Nil, None)),
                 BCP47.parseTag("sr-Cyrl")
    )
    // sr-Latn (Serbian written using the Latin script)
    assertEquals(Option[BCP47Tag](LanguageTag("sr", None, Some("Latn"), None, Nil, Nil, None)),
                 BCP47.parseTag("sr-Latn")
    )
  }

  test("test_languages_extended_samples") {
    // Extended language subtags:
    // zh-cmn-Hans-CN (Chinese, Mandarin, Simplified script, as used in China)
    assertEquals(
      Option[BCP47Tag](LanguageTag("zh", Some("cmn"), Some("Hans"), Some("CN"), Nil, Nil, None)),
      BCP47.parseTag("zh-cmn-Hans-CN")
    )
    // cmn-Hans-CN (Mandarin Chinese, Simplified script, as used in China)
    assertEquals(
      Option[BCP47Tag](LanguageTag("cmn", None, Some("Hans"), Some("CN"), Nil, Nil, None)),
      BCP47.parseTag("cmn-Hans-CN")
    )
    // zh-yue-HK (Chinese, Cantonese, as used in Hong Kong SAR)
    assertEquals(Option[BCP47Tag](LanguageTag("zh", Some("yue"), None, Some("HK"), Nil, Nil, None)),
                 BCP47.parseTag("zh-yue-HK")
    )
    // yue-HK (Cantonese Chinese, as used in Hong Kong SAR)
    assertEquals(Option[BCP47Tag](LanguageTag("yue", None, None, Some("HK"), Nil, Nil, None)),
                 BCP47.parseTag("yue-HK")
    )
  }

  test("test_language_script_region_samples") {
    // Language-Script-Region:
    // zh-Hans-CN (Chinese written using the Simplified script as used in mainland China)
    assertEquals(
      Option[BCP47Tag](LanguageTag("zh", None, Some("Hans"), Some("CN"), Nil, Nil, None)),
      BCP47.parseTag("zh-Hans-CN")
    )
    // sr-Latn-RS (Serbian written using the Latin script as used in Serbia)
    assertEquals(
      Option[BCP47Tag](LanguageTag("sr", None, Some("Latn"), Some("RS"), Nil, Nil, None)),
      BCP47.parseTag("sr-Latn-RS")
    )
  }

  test("test_language_variant_samples") {
    // Language-Variant:
    // sl-rozaj (Resian dialect of Slovenian)
    assertEquals(Option[BCP47Tag](LanguageTag("sl", None, None, None, List("rozaj"), Nil, None)),
                 BCP47.parseTag("sl-rozaj")
    )
    // sl-rozaj-biske (San Giorgio dialect of Resian dialect of Slovenian)
    assertEquals(
      Option[BCP47Tag](LanguageTag("sl", None, None, None, List("rozaj", "biske"), Nil, None)),
      BCP47.parseTag("sl-rozaj-biske")
    )
    // sl-nedis (Nadiza dialect of Slovenian)
    assertEquals(Option[BCP47Tag](LanguageTag("sl", None, None, None, List("nedis"), Nil, None)),
                 BCP47.parseTag("sl-nedis")
    )
  }

  test("test_language_region_variant_samples") {
    // Language-Region-Variant:
    // de-CH-1901 (German as used in Switzerland using the 1901 variant [orthography])
    assertEquals(
      Option[BCP47Tag](LanguageTag("de", None, None, Some("CH"), List("1901"), Nil, None)),
      BCP47.parseTag("de-CH-1901")
    )
    // sl-IT-nedis (Slovenian as used in Italy, Nadiza dialect)
    assertEquals(
      Option[BCP47Tag](LanguageTag("sl", None, None, Some("IT"), List("nedis"), Nil, None)),
      BCP47.parseTag("sl-IT-nedis")
    )
  }

  test("test_language_script_region_variant_samples") {
    // Language-Script-Region-Variant:
    // hy-Latn-IT-arevela (Eastern Armenian written in Latin script, as used in Italy)
    assertEquals(
      Option[BCP47Tag](
        LanguageTag("hy", None, Some("Latn"), Some("IT"), List("arevela"), Nil, None)
      ),
      BCP47.parseTag("hy-Latn-IT-arevela")
    )
  }

  test("test_language_region_samples") {
    // Language-Region:
    // de-DE (German for Germany)
    assertEquals(Option[BCP47Tag](LanguageTag("de", None, None, Some("DE"), Nil, Nil, None)),
                 BCP47.parseTag("de-DE")
    )
    // en-US (English as used in the United States)
    assertEquals(Option[BCP47Tag](LanguageTag("en", None, None, Some("US"), Nil, Nil, None)),
                 BCP47.parseTag("en-US")
    )
    // es-419 (Spanish appropriate for the Latin America and Caribbean
    // region using the UN region code)
    assertEquals(Option[BCP47Tag](LanguageTag("es", None, None, Some("419"), Nil, Nil, None)),
                 BCP47.parseTag("es-419")
    )
  }

  test("test_private_use_samples") {
    // Private use subtags:
    // de-CH-x-phonebk
    assertEquals(
      Option[BCP47Tag](LanguageTag("de", None, None, Some("CH"), Nil, Nil, Some("phonebk"))),
      BCP47.parseTag("de-CH-x-phonebk")
    )
    // az-Arab-x-AZE-derbend
    assertEquals(
      Option[BCP47Tag](LanguageTag("az", None, Some("Arab"), None, Nil, Nil, Some("AZE-derbend"))),
      BCP47.parseTag("az-Arab-x-AZE-derbend")
    )
  }

  test("test_private_use_tag") {
    // Private use registry values:
    // x-whatever (private use using the singleton 'x')
    assertEquals(Option[BCP47Tag](PrivateUseTag("whatever")), BCP47.parseTag("x-whatever"))
  }

  test("test_extensions_samples") {
    // Tags that use extensions:
    // en-US-u-islamcal
    assertEquals(
      Option[BCP47Tag](LanguageTag("en", None, None, Some("US"), Nil, List("u-islamcal"), None)),
      BCP47.parseTag("en-US-u-islamcal")
    )
    // zh-CN-a-myext-x-private
    assertEquals(
      Option[BCP47Tag](
        LanguageTag("zh", None, None, Some("CN"), Nil, List("a-myext"), Some("private"))
      ),
      BCP47.parseTag("zh-CN-a-myext-x-private")
    )
    // en-a-myext-b-another
    assertEquals(Option[BCP47Tag](
                   LanguageTag("en", None, None, None, Nil, List("a-myext", "b-another"), None)
                 ),
                 BCP47.parseTag("en-a-myext-b-another")
    )
  }

  test("test_invalid_samples") {
    // Tags that use extensions:
    // de-419-DE (two region tags)
    assertEquals(None: Option[BCP47Tag], BCP47.parseTag("de-419-DE"))
    // a-DE (use of a single-character subtag in primary position; note
    // that there are a few grandfathered tags that start with "i-" that
    // are valid)
    assertEquals(None: Option[BCP47Tag], BCP47.parseTag("a-DE"))
  }
}
