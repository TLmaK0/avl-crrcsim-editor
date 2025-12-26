import sbt.ProjectRef

lazy val crrcSimEditor = project
  .in(file("."))

name := "Avl CRRCSim Editor"

version := "0.7.0"

scalaVersion := "2.10.4"

resolvers += "swt-repo" at "https://swt-repo.googlecode.com/svn/repo/"
resolvers += "jogamp-remote" at "https://jogamp.org/deployment/maven"

Compile / run / mainClass := Some("com.abajar.crrcsimeditor.Main")

enablePlugins(JavaAppPackaging)

maintainer := "Hugo Freire <hfreire@abajar.com>"

ThisBuild / scalacOptions ++= Seq("-feature")

// javacOptions ++= Seq("-Xlint:unchecked")
scalacOptions ++= Seq("-language:postfixOps", "-language:reflectiveCalls")

run / fork := true

run / envVars := Map("SWT_GTK3" -> "0")

val swtVersion = "4.3"
val osName = System.getProperty("os.name").toLowerCase

val swtDependency: Option[sbt.ModuleID] = {
  if (osName.contains("linux"))
    Some("org.eclipse.swt" % "org.eclipse.swt.gtk.linux.x86_64" % swtVersion)
  else if (osName.contains("windows"))
    Some("org.eclipse.swt" % "org.eclipse.swt.win32.win32.x86_64" % swtVersion)
  else if (osName.contains("mac"))
    Some("org.eclipse.swt" % "org.eclipse.swt.cocoa.macosx.x86_64" % swtVersion)
  else
    None
}

libraryDependencies ++= Seq(
  "org.eclipse.persistence" % "org.eclipse.persistence.moxy" % "2.5.2",
  "junit" % "junit" % "4.12",
  "javax.xml.bind" % "jaxb-api" % "2.3.1",
  "org.jogamp.gluegen" % "gluegen-rt-main" % "2.5.0",
  "org.jogamp.jogl" % "jogl-all-main" % "2.5.0",
  "org.yaml" % "snakeyaml" % "2.0"
) ++ swtDependency.toSeq
