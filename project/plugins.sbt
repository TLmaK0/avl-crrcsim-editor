lazy val root = (project in file("."))

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0-M5")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "1.5.1-2-g8b57b53")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
