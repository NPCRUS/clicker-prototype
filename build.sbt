name := "clicker-prototype"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.0",
  "com.pauldijou" %% "jwt-spray-json" % "4.2.0",

  "com.typesafe.akka" %% "akka-http"   % "10.1.11",
  "com.typesafe.akka" %% "akka-stream" % "2.5.31",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",

  "com.typesafe.slick" %% "slick" % "3.3.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.2.5",

  "org.scalatest" %% "scalatest" % "3.2.0" % "test"
)
