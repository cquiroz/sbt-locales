# sbt-locales

This is an sbt plugin that can create custom locales databases to be used with scala-java-locales.
The db is build out of the cldr database definition:

http://cldr.unicode.org/

The database is fairly large which produced very big files that may not be needed for your application,
instead you can build a customized version containing just the data you need, e.g. just date formats
for english and spanish


