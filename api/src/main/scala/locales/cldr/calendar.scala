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

/** Interfaces describing calendar data */
case class Calendar(id: String) {
  def safeName: String = id.replace("-", "_")
}

case class CalendarSymbols(
  months:        List[String],
  shortMonths:   List[String],
  weekdays:      List[String],
  shortWeekdays: List[String],
  amPm:          List[String],
  eras:          List[String]
)

