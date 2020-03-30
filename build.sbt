name := """zio"""
organization := "com.dekopay"

version := "1.0-SNAPSHOT"

val zioVersion = "1.0.0-RC18-2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "dev.zio" %% "zio" % zioVersion
libraryDependencies += "dev.zio" %% "zio-macros-core" % "0.6.2"
libraryDependencies += "org.mockito" % "mockito-core" % "3.3.3"

libraryDependencies += ws

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test" % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt" % zioVersion % "test",
  "dev.zio" %% "zio-test-magnolia" % zioVersion % "test" // optional
)
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

Global / onChangedBuildSource := ReloadOnSourceChanges

scalacOptions += "-Ymacro-annotations"