import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import utils.AppConfig

import scala.concurrent.ExecutionContext

object WebServer
  extends App
    with AppConfig {
  implicit val system: ActorSystem = ActorSystem("web-server")
  implicit val executionContext: ExecutionContext = system.dispatcher
  sys.addShutdownHook(system.terminate())

  val bindingFuture = Http().bindAndHandle(Router(), config.getString("host"), config.getString("port").toInt)
}
