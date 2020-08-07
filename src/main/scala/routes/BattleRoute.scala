package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import config.AppConfig
import config.AppConfig.db
import game._
import game.items._
import models._
import spray.json._
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
      for {
        character <- db.run(CharacterModel.getCharacter(user)).map(CharacterModel.toDbCharacter)
        equippedItems <- db.run(InventoryModel.getItemsByIds(character.getIds)).map(_.map(InventoryModel.toDbItem))
      } yield (user, equippedItems)
    }.map { res =>
      val (user, dbItems) = res

      val items = dbItems.toList.map(_.toJson.convertTo[Item])
      val (handle, armorSet) = prepareWearables(items)
      val character = Pawn(user.userId.toString, handle, armorSet, InitialProperties())

      val enemyBot = Generator.generateBotEnemy(character, battlePost.mapLevel)

      val battle = new Battle(character, enemyBot)
      val actions = battle.calculate()
      actions
    }
  }

  private def prepareWearables(items: List[Item]): (Handle, ArmorSet) = {
    def wearArmor(items: List[Armor], armorSet: ArmorSet): ArmorSet =
      if(items.isEmpty) armorSet
      else wearArmor(items.tail, armorSet.wear(items.head))


    val armors = items.collect {
      case a: Armor => a
    }
    val armorSet = wearArmor(armors, ArmorSet.empty)

    val weapon = (items.collectFirst {
      case a: Weapon => a
    }).get
    val shield = armors collectFirst {
      case s: Shield => s
    }

    val handle = if(weapon.twoHanded) TwoHandedHandle(weapon)
    else OneHandedHandle(weapon, shield)

    (handle, armorSet)
  }
}
