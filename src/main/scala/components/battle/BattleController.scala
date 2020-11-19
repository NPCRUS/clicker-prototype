package components.battle

import game._
import models.{BattlePost, BattleResult, DbItem, InventoryModel, Transactions, User, UserModel}
import utils.AppConfig
import utils.AppExceptions.MapLevelExcessException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BattleController
  extends AppConfig {

  def battle(user: User, battlePost: BattlePost): Future[BattleResult] = {
    if (battlePost.mapLevel > user.maxMapLevel) throw new MapLevelExcessException
    else {
      Transactions.getCharacterWithDbItems(user).map((_, user)) flatMap { res =>
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

        // generate reward
        if(actions.last.init.pawn == character) {
          val item = Generator.generateReward(battlePost.mapLevel)
          item match {
            case Some(item) =>
              for {
                item <- InventoryModel.insert(DbItem.fromGameItem(item, user))
                _ <- if(user.maxMapLevel == battlePost.mapLevel) {
                  db.run(UserModel.upsert(user.copy(maxMapLevel = user.maxMapLevel + 1)))
                } else {
                  Future.successful(0)
                }
              } yield BattleResult(actions, List(item))
            case _ => Future.successful(BattleResult(actions, List.empty))
          }
        } else {
          Future.successful(BattleResult(actions, List.empty))
        }
      }
    }
  }
}
