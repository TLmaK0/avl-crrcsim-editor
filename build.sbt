name := "Avl CRRCSim Editor"

version := "0.7.0"

scalaVersion := "2.10.4"

resolvers += "swt-repo" at "https://swt-repo.googlecode.com/svn/repo/"

libraryDependencies += "org.eclipse.swt" % "org.eclipse.swt.win32.win32.x86_64" % "4.4"

mainClass in (Compile,run) := Some("com.abajar.crrcsimeditor.Main")

enablePlugins(JavaAppPackaging)

maintainer := "Hugo Freire <hfreire@abajar.com>"

scalacOptions in ThisBuild ++= Seq("-feature")

// javacOptions ++= Seq("-Xlint:unchecked")
scalacOptions ++= Seq("-language:postfixOps", "-language:reflectiveCalls")

fork in run := true
