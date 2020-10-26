package components.battle

import game._
import models.{BattlePost, Transactions, User}
import utils.AppConfig
import utils.AppExceptions.MapLevelExcessException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BattleController
  extends AppConfig {

  def battle(user: User, battlePost: BattlePost): Future[List[Action]] = {
    if (battlePost.mapLevel > user.maxMapLevel) throw new MapLevelExcessException
    else {
      Transactions.getCharacterWithDbItems(user).map((_, user)) map { res =>
        val (characterWithDbItems, user) = res
        val characterWithItems = characterWithDbItems.toDbCharacterWithItems
        val character = Pawn(
          user.userId.toString,
          characterWithItems.handle,
          characterWithItems.armorSet,
          InitialProperties()
        )

        val enemyBot = Generator.generateBotEnemy(battlePost.mapLevel)

        val battle = new Battle(character, enemyBot)
        val actions = battle.calculate()
        actions
      }
    }
  }
}
