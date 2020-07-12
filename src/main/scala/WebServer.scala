import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.concurrent.ExecutionContext

object WebServer extends App {
  implicit val system: ActorSystem = ActorSystem("web-server")
  implicit val executionContext: ExecutionContext = system.dispatcher
  sys.addShutdownHook(system.terminate())

  val bindingFuture = Http().bindAndHandle(Router(), "localhost", 80)
}
