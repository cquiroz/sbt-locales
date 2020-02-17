package locales

package object cldr {
  val datePatternsFormatIdx: Map[Int, String] =
    Map(0 -> "full", 1 -> "long", 2 -> "medium", 3 -> "short")
  val datePatternsFormat: Map[String, Int] =
    datePatternsFormatIdx.map(_.swap)

}

