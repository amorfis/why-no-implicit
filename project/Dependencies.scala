import sbt._

object Dependencies {

  val scalaTestVersion = "3.2.13"
  val mockitoVersion = "4.7.0"

  lazy val deps: Seq[ModuleID] =
    Seq(
      "com.chuusai" %% "shapeless" % "2.3.9",
      "org.mockito" % "mockito-core" % mockitoVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion,
      "org.mockito" %% "mockito-scala" % "1.17.12"
    )
}
