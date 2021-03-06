name := """shopper-react"""
organization := "sonac"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.outworkers" %% "phantom-dsl" % "2.12.1",
  "com.outworkers" %% "phantom-streams" % "2.12.1",
  "com.outworkers" %% "util-testing" % "0.30.1",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.scala-lang" % "scala-compiler" % "2.12.3",
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc41")

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "sonac.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "sonac.binders._"
