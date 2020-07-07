package routes

import com.typesafe.config.{Config, ConfigFactory}

object AppConfig {
  lazy val config: Config = ConfigFactory.load().getConfig("app")
}
