name := "clicker-prototype"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % "10.1.11",
  "com.typesafe.akka" %% "akka-stream" % "2.5.31",

  "org.scalatest" %% "scalatest" % "3.2.0" % "test"
)
