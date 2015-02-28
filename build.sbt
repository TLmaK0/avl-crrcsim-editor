lazy val root = (project in file(".")).
  settings(
    name := "avl-crrcsim-editor",
    version := "0.6.0",
    scalaVersion := "2.10.4"
  )

mainClass in (Compile,run) := Some("com.abajar.crrcsimeditor.CRRCsimEditor")
