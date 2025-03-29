lazy val root = (project in file("."))

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.6")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
