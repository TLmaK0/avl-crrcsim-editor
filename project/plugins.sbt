lazy val root = (project in file(".")).dependsOn(sbtHeaders)

lazy val sbtHeaders = ProjectRef(uri("git://github.com/TLmaK0/sbt-header.git#feature/allow_multiplatform_newline"), "sbtHeader")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0-M5")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
