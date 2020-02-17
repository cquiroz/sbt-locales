package locales

import cats.implicits._

// Selection of Numbering Systems
sealed trait NumberingSystemFilter extends Product with Serializable {
  def filter: String => Boolean
}

object NumberingSystemFilter {
  case object None extends NumberingSystemFilter {
    def filter: String => Boolean = _ === "latn"
  }
  case object All extends NumberingSystemFilter {
    def filter: String => Boolean = _ => true
  }
  final case class Selection(s: List[String]) extends NumberingSystemFilter {
    def filter: String => Boolean = c => c === "latn" || s.contains(c)
  }

  object Selection {
    def apply(s: String): Selection = Selection(List(s))
  }

}

// Selection of Calendars
sealed trait CalendarFilter extends Product with Serializable {
  def filter: String => Boolean
}

object CalendarFilter {
  case object None extends CalendarFilter {
    def filter: String => Boolean = _ => false
  }
  case object All extends CalendarFilter {
    def filter: String => Boolean = _ => true
  }
  final case class Selection(s: List[String]) extends CalendarFilter {
    def filter: String => Boolean = s.contains
  }

  object Selection {
    def apply(s: String): Selection = Selection(List(s))
  }

}

// Selection of Locales
sealed trait LocalesFilter extends Product with Serializable {
  def filter: String => Boolean
}

object LocalesFilter {
  case object None extends LocalesFilter {
    def filter: String => Boolean = _ => false
  }
  case object All extends LocalesFilter {
    def filter: String => Boolean = _ => true
  }
  final case class Selection(s: List[String]) extends LocalesFilter {
    def filter: String => Boolean = s.contains
  }

  object Selection {
    def apply(s: String): Selection = Selection(List(s))
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
    def apply(s: String): Selection = Selection(List(s))
  }

}

// Selection of Currencies
sealed trait CurrencyRegionFilter extends Product with Serializable {
  def filter: String => Boolean
}

object CurrencyRegionFilter {
  case object None extends CurrencyRegionFilter {
    def filter: String => Boolean = _ => false
  }
  case object All extends CurrencyRegionFilter {
    def filter: String => Boolean = _ => true
  }
  final case class Selection(s: List[String]) extends CurrencyRegionFilter {
    def filter: String => Boolean = s.contains
  }

  object Selection {
    def apply(s: String): Selection = Selection(List(s))
  }

}
final case class Filters(
  localesFilter:          LocalesFilter,
  nsFilter:               NumberingSystemFilter,
  calendarFilter:         CalendarFilter,
  currencyFilter:         CurrencyFilter,
  currencyRegionFilter:   CurrencyRegionFilter,
  supportDateTimeFormats: Boolean,
  supportNumberFormats:   Boolean
)
