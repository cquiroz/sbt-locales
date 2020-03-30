package locales

import cats.implicits._

sealed trait CLDRVersion extends Product with Serializable {
  def id: String
}

object CLDRVersion {
  case object LatestVersion extends CLDRVersion {
    val id: String = "latest"
  }
  final case class Version(version: String) extends CLDRVersion {
    val id: String = version
  }
}

// Selection of Numbering Systems
sealed trait NumberingSystemFilter extends Product with Serializable {
  def filter: String => Boolean
}

object NumberingSystemFilter {
  case object Minimal extends NumberingSystemFilter {
    def filter: String => Boolean = _ === "latn"
  }
  case object All extends NumberingSystemFilter {
    def filter: String => Boolean = _ => true
  }
  final case class Selection(s: List[String]) extends NumberingSystemFilter {
    def filter: String => Boolean = c => c === "latn" || s.contains(c)
  }

  object Selection {
    def apply(s: String*): Selection = Selection(s.toList)
  }

}

// Selection of Calendars
sealed trait CalendarFilter extends Product with Serializable {
  def filter: String => Boolean
}

object CalendarFilter {
  case object Minimal extends CalendarFilter {
    def filter: String => Boolean = _ === "gregorian"
  }
  case object All extends CalendarFilter {
    def filter: String => Boolean = _ => true
  }
  final case class Selection(s: List[String]) extends CalendarFilter {
    def filter: String => Boolean = c => c === "gregorian" || s.contains(c)
  }

  object Selection {
    def apply(s: String*): Selection = Selection(s.toList)
  }

}

// Selection of Locales
sealed trait LocalesFilter extends Product with Serializable {
  def filter: String => Boolean
}

object LocalesFilter {
  case object Minimal extends LocalesFilter {
    def filter: String => Boolean = l => l === "root" || l === "en"
  }
  case object All extends LocalesFilter {
    def filter: String => Boolean = _ => true
  }
  final case class Selection(s: List[String]) extends LocalesFilter {
    def filter: String => Boolean =
      l => l === "root" || l === "en" || s.map(_.replaceAll("-", "_")).contains(l)
  }

  object Selection {
    def apply(s: String*): Selection = Selection(s.toList)
  }

}

// Selection of Currencies
sealed trait CurrencyFilter extends Product with Serializable {
  def filter: String => Boolean
}

object CurrencyFilter {
  case object None extends CurrencyFilter {
    def filter: String => Boolean = _ => false
  }
  case object All extends CurrencyFilter {
    def filter: String => Boolean = _ => true
  }
  final case class Selection(s: List[String]) extends CurrencyFilter {
    def filter: String => Boolean = s.contains
  }

  object Selection {
    def apply(s: String*): Selection = Selection(s.toList)
  }

}

final case class Filters(
  localesFilter:          LocalesFilter,
  nsFilter:               NumberingSystemFilter,
  calendarFilter:         CalendarFilter,
  currencyFilter:         CurrencyFilter,
  supportDateTimeFormats: Boolean,
  supportNumberFormats:   Boolean,
  supportISOCodes:        Boolean
)
