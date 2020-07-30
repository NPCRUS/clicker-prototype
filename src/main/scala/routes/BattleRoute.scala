package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import config.AppConfig
import game._
import game.items._
import models._
import models.JsonSupport._
import util.AppExceptions
import util.AppExceptions._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class BattleRoute {
  import game.JsonSupport._

  def getRoutes: Route = path("battle") {
    Authenticate.customAuthorization { token =>
      concat {
        post {
          entity(as[BattlePost])(battlePost =>
            onComplete(battle(token, battlePost)) {
              case Success(result) => complete(result)
              case Failure(exception) => throw exception
            }
          )
        }
      }
    }
  }

  private def battle(token: Token, battlePost: BattlePost): Future[List[Action]] = {
    AppConfig.db.run(Schemas.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) =>
        if(battlePost.mapLevel <= u.maxMapLevel) u
        else throw new MapLevelExcessException
      case None => throw new UserNotFound
    }.map { user =>
       //instead of this we should retrieve character and his inventory and convert it to a pawn
      val knife = Dagger(
        "knife",
        cd = 10000,
        20,
        DamageType.Physical,
        List(PassiveEffect(EffectTargetType.ColdRes, 1000)),
        List(PeriodicActiveEffect(ActiveEffectType.Periodic, EffectTargetType.Hp, chance = 1.0, change = 10, self = true, 4, 2000))
      )
      val handle1 = OneHandedHandle(knife, None)
      val character = Pawn("Mate", handle1, ArmorSet.empty, InitialProperties())

      val enemyBot = Generator.generateBotEnemy(character, battlePost.mapLevel)

      val battle = new Battle(character, enemyBot)
      val actions = battle.calculate()
      actions
    }
  }
}
