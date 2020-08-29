package components.battle

import game.{Action, Battle, Generator, InitialProperties, Pawn}
import models.{BattlePost, Token, Transactions, UserModel}
import util.AppConfig
import util.AppExceptions.{MapLevelExcessException, UserNotFound}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BattleController
  extends AppConfig {

  def battle(token: Token, battlePost: BattlePost): Future[List[Action]] = {
    db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
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
