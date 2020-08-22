package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import config.AppConfig
import game._
import game.items._
import models._
import models.JsonSupport._
import util.AppExceptions._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class BattleRoute {
  import game.JsonSupport._

  def getRoutes: Route = path("battle") {
    Authenticate.customAuthorization { token =>
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

  private def battle(token: Token, battlePost: BattlePost): Future[List[Action]] = {
    AppConfig.db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) =>
        if(battlePost.mapLevel <= u.maxMapLevel) u
        else throw new MapLevelExcessException
      case None => throw new UserNotFound
    }.flatMap { user =>
      Transactions.getCharacterWithDbItems(user)
        .map((_, user))
    }.map { res =>
      val (characterWithDbItems, user) = res
      val characterWithItems = characterWithDbItems.toDbCharacterWithItems
      val character = Pawn(
        user.userId.toString,
        characterWithItems.handle,
        characterWithItems.armorSet,
        InitialProperties()
      )

      val enemyBot = Generator.generateBotEnemy(character, battlePost.mapLevel)

      val battle = new Battle(character, enemyBot)
      val actions = battle.calculate()
      actions
    }
  }
}
