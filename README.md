# sbt-locales

![build](https://github.com/cquiroz/sbt-locales/workflows/build/badge.svg)

An sbt plugin that can create custom locales databases to be used with [scala-java-locales](https://github.com/cquiroz/scala-java-locales)
and [scala-java-time](https://github.com/cquiroz/scala-java-time)

The db is build out of the cldr database definition:

http://cldr.unicode.org/

The database is fairly large which produced very big files that may not be needed for your application,
instead you can build a customized version containing just the data you need, e.g. just date formats
for english and spanish

# Usage

Include on `project/plugins.sbt`

```scala
addSbtPlugin("io.github.cquiroz" % "sbt-locales" % "2.0.1")
```

# Configuration
Most applications just need a few locales and may need for example only dates.
The code generated will thus include only the minimal data

* *cldrVersion*: A version of CLDR is recommended otherwise it defaults to the latest at the time of publication.
* *localesFilter*: **Default includes english**. You can add locales by tag, e.g.
 `en-US`, `fi`, `es-CL`. Note that `root` and` en` are always included. If you include a
 locale for a country you'd need to also include its parent, e.g. to include `fi-FI` you
 should also include `fi`
* *currencyFilter*: **Default none**. If you need currencies include them on this list
* *nsFilter*: **Default latn**. List of numbering systems, `latn` is always included
* *calendarFilter*: **Default gregorian**. List of calendars, `gregorian` is always included
* *supportDateTimeFormats*: **Default true**. Whether to include date time formats
* *supportNumberFormats*: **Default false**. Whether to include number formats
* *supportISOCodes*: **Default false**. ISO codes list will be generated accordingly

# Example:

```scala
  .enablePlugins(LocalesPlugin)
  .settings(
      localesFilter := LocalesFilter.Selection("en-US", "fi", "fi-FI"),
      nsFilter := NumberingSystemFilter.Minimal,
      currencyFilter := CurrencyFilter.Selection("EUR"),
      supportISOCodes := true,
      supportNumberFormats := true
  )
```

# Troubleshooting

Note that the plugin doesn't verify consistency, if e.g. your locale requires arabic
numbers and you don't include the arabic numbering system you may get compilation
errors

# Example

See [scalajs-locales-demo](https://github.com/cquiroz/scalajs-locales-demo)
