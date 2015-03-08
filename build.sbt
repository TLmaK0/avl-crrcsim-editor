name := "Avl CRRCSim Editor"

version := "0.6.0"

scalaVersion := "2.10.4"

mainClass in (Compile,run) := Some("Main")

enablePlugins(JavaAppPackaging)

maintainer := "Hugo Freire <hfreire@abajar.com>"

fork in run := true

libraryDependencies += "org.eclipse.persistence" % "org.eclipse.persistence.moxy" % "2.5.2"
