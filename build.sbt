name := "clicker-prototype"

version := "0.1"

scalaVersion := "2.13.3"

assemblyJarName in assembly := "clicker-prototype.jar"
mainClass in assembly := Some("WebServer")

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.0",
  "com.pauldijou" %% "jwt-spray-json" % "4.2.0",

  "com.typesafe.akka" %% "akka-http"   % "10.1.12",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.12",
  "com.typesafe.akka" %% "akka-stream" % "2.5.31",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12",
  "ch.megard" %% "akka-http-cors" % "1.0.0",

  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.postgresql" % "postgresql" % "42.2.5",
  "com.github.tminglei" %% "slick-pg" % "0.19.2",
  "com.github.tminglei" %% "slick-pg_spray-json" % "0.19.2",

  "org.scalatest" %% "scalatest" % "3.2.0" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.12" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.31" % Test,
  "org.scalamock" %% "scalamock" % "5.0.0" % Test
)
