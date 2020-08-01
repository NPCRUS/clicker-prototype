package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{PathMatcher, Route}
import config.AppConfig
import game.items.{Armor, Item, ItemType, Weapon}
import models._
import models.JsonSupport._
import util.MyPostgresProfile.api._
import util.AppExceptions

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class InventoryRoute {
  import game.JsonSupport._

  val equipRouteMatcher: PathMatcher[Tuple1[Int]] = "inventory" / IntNumber / "equip"

  def getRoutes: Route = Authenticate.customAuthorization { token =>
    path("inventory") {
      get {
        onComplete(getInventory(token)) {
          case Success(result) =>
            complete(result)
          case Failure(exception) => throw exception
        }
      } ~
      post {
        entity(as[Item]) { item =>
          onComplete(createItem(token, item)) {
            case Success(item) =>
              complete(item)
            case Failure(exception) => throw exception
          }
        }
      }
    } ~
    path(equipRouteMatcher) { i: Int =>
      onComplete(equipItem(token, i)) {
        case Success(_) => complete(StatusCodes.OK)
        case Failure(exception) => throw exception
      }
    }
  }

  private def equipItem(token: Token, itemId: Int): Future[Unit] = {
    AppConfig.db.run(UserModel.getUserByUserId(token.user_id.toInt)).flatMap {
      case Some(u) => Future(UserModel.toUser(u))
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      for {
        item <- AppConfig.db.run(InventoryModel.getItemByIdAndUser(itemId, user))
        equippedItems <- AppConfig.db.run(InventoryModel.getEquippedItems(user))
      } yield (item, equippedItems)
    }.map { res =>
      val (item, eqItems) = res
      if(item.isEmpty) throw new AppExceptions.ItemNotFound
      else if(item.get.equipped) throw new AppExceptions.ItemIsAlreadyEquipped
      else {
        val dbItem = InventoryModel.toDbItem(item.get)
        val equippedItems = eqItems.toList.map(i => InventoryModel.toDbItem(i))
        val unequip = itemsToUnequip(dbItem, equippedItems)

        val dbio = DBIO.seq(
          InventoryModel.updateEquipped(List(dbItem),true),
          InventoryModel.updateEquipped(unequip, false)
        ).transactionally

        AppConfig.db.run(dbio)
      }
    }
  }

  private def createItem(token: Token, item: Item): Future[DbItem] = {
    def armorToDbItem(a: Armor, u: User): DbItem =
      DbItem(None, a.name, a.cd, a._type.toString, a.passiveEffects, a.activeEffects, u.id.get, Some(a.armor), Some(a.armorType.toString), None, None, None, None)

    def weaponToDbItem(w: Weapon, u: User): DbItem =
      DbItem(None, w.name, w.cd, w._type.toString, w.passiveEffects, w.activeEffects, u.id.head, None, None, Some(w.weaponType.toString), Some(w.baseDamage), Some(w.twoHanded), Some(w.damageType.toString))

    AppConfig.db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      val dbItem = item match {
        case a: Armor => armorToDbItem(a, user)
        case w: Weapon => weaponToDbItem(w, user)
      }
      InventoryModel.insert(dbItem)
    }
  }

  private def getInventory(token: Token): Future[List[DbItem]] = {
    AppConfig.db.run(UserModel.getUserByUserId(token.user_id.toInt)).map {
      case Some(u) => u
      case None => throw new AppExceptions.UserNotFound
    }.flatMap { user =>
      AppConfig.db.run(InventoryModel.getUserInventory(user))
    }.map { items =>
      items.toList.map(i => InventoryModel.toDbItem(i))
    }
  }

  // TODO: implement mainHand, offhand equip
  private def itemsToUnequip(targetItem: DbItem, equippedItems: List[DbItem]): List[DbItem] = {
    // TODO: this match string literals should be replaces with something better
    targetItem._type match {
      case "Armor" =>
        equippedItems.filter(_.armorType.isDefined).filter(_.armorType == targetItem.armorType)
      case "Weapon" =>
        equippedItems.filter(_._type == ItemType.Weapon.toString)
    }
  }
}
