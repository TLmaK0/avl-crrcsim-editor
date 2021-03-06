import de.heikoseeberger.sbtheader.HeaderPattern

import sbt.ProjectRef

lazy val crrcSimEditor = project
  .in(file("."))

name := "Avl CRRCSim Editor"

version := "0.7.0"

scalaVersion := "2.10.4"

resolvers += "swt-repo" at "https://swt-repo.googlecode.com/svn/repo/"

mainClass in (Compile,run) := Some("com.abajar.crrcsimeditor.Main")

enablePlugins(JavaAppPackaging)

maintainer := "Hugo Freire <hfreire@abajar.com>"

scalacOptions in ThisBuild ++= Seq("-feature")

// javacOptions ++= Seq("-Xlint:unchecked")
scalacOptions ++= Seq("-language:postfixOps", "-language:reflectiveCalls")

fork in run := true

libraryDependencies += "org.eclipse.persistence" % "org.eclipse.persistence.moxy" % "2.5.2"

libraryDependencies += "org.eclipse.swt" % "org.eclipse.swt.win32.win32.x86_64" % "4.4"

libraryDependencies += "junit" % "junit" % "4.12"

headers := Map(
  "scala" -> (
    HeaderPattern.cStyleBlockComment,
    new java.util.Scanner(new java.io.File("LICENSE_HEADER")).useDelimiter("\\z").next()
  ),
  "java" -> (
    HeaderPattern.cStyleBlockComment,
    new java.util.Scanner(new java.io.File("LICENSE_HEADER")).useDelimiter("\\z").next()
  )
)
