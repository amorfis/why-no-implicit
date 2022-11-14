import sbt.Keys.{scalacOptions, updateOptions}

name := "why-no-implicit"
ThisBuild / scalaVersion := "2.13.10"
updateOptions := updateOptions.value.withGigahorse(false)
// Enable splain error messages - now integrated since scala 2.13.6
scalacOptions ++= Seq("-Vimplicits", "-Vtype-diffs")
libraryDependencies ++= Dependencies.deps
updateOptions := updateOptions.value.withGigahorse(false)
scalacOptions ++= Seq("-Vimplicits", "-Vtype-diffs")

Test / fork := true
Test / parallelExecution := false
