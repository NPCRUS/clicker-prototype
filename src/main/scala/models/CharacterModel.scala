package models
import config.AppConfig._
import game.items.{ActiveEffect, Body, Helmet, PassiveEffect}
import util.MyPostgresProfile.api._

import scala.concurrent.Future

object CharacterModel extends TableQuery(new Character(_)){

  def getCharacter(user: User) =
    this.filter(_.userId === user.id).result.head

  def create(userId: Int) =
    this += DbCharacter(None, userId, None, None, None, None, None, None, None, None, None, None)

  def upsert(character: DbCharacter) = {
    this.insertOrUpdate(character)
  }

  def toDbCharacter(c: Character#TableElementType): DbCharacter =
    DbCharacter(
      c.id,
      c.userId,
      c.helmet,
      c.body,
      c.gloves,
      c.boots,
      c.belt,
      c.amulet,
      c.ring1,
      c.ring2,
      c.mainHand,
      c.offHand
    )
}

class Character(tag: Tag) extends Table[DbCharacter](tag, "character") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("user_id")
  def helmet = column[Int]("helmet")
  def body = column[Int]("body")
  def gloves = column[Int]("gloves")
  def boots = column[Int]("boots")
  def belt = column[Int]("belt")
  def amulet = column[Int]("amulet")
  def ring1 = column[Int]("ring1")
  def ring2 = column[Int]("ring2")
  def mainHand = column[Int]("main_hand")
  def offHand = column[Int]("off_hand")

  def * =
    (id.?, userId, helmet.?, body.?, gloves.?, boots.?, belt.?, amulet.?, ring1.?, ring2.?, mainHand.?, offHand.?)
      .mapTo[DbCharacter]

  def user = foreignKey("character_fk", userId, UserModel)(_.id, onUpdate = ForeignKeyAction.Cascade)
}

case class DbCharacter(
  id: Option[Int],
  userId: Int,
  helmet: Option[Int],
  body: Option[Int],
  gloves: Option[Int],
  boots: Option[Int],
  belt: Option[Int],
  amulet: Option[Int],
  ring1: Option[Int],
  ring2: Option[Int],
  mainHand: Option[Int],
  offHand: Option[Int]
) {
  def getIds: Seq[Int] =
    Seq(helmet, body, gloves, boots, belt, amulet, ring1, ring2, mainHand, offHand)
      .filter(_.isDefined)
      .map(_.get)

  def equipArmor(itemId: Int, equipmentPart: EquipmentPart.Type) =
    equipmentPart match {
      case EquipmentPart.Helmet =>
        DbCharacter(id, userId, Some(itemId), body, gloves, boots, belt, amulet, ring1, ring2, mainHand, offHand)
      case EquipmentPart.Body =>
        DbCharacter(id, userId, helmet, Some(itemId), gloves, boots, belt, amulet, ring1, ring2, mainHand, offHand)
      case EquipmentPart.Gloves =>
        DbCharacter(id, userId, helmet, body, Some(itemId), boots, belt, amulet, ring1, ring2, mainHand, offHand)
      case EquipmentPart.Boots =>
        DbCharacter(id, userId, helmet, body, gloves, Some(itemId), belt, amulet, ring1, ring2, mainHand, offHand)
      case EquipmentPart.Belt =>
        DbCharacter(id, userId, helmet, body, gloves, boots, Some(itemId), amulet, ring1, ring2, mainHand, offHand)
      case EquipmentPart.Amulet =>
        DbCharacter(id, userId, helmet, body, gloves, boots, belt, Some(itemId), ring1, ring2, mainHand, offHand)
      case EquipmentPart.Ring1 =>
        DbCharacter(id, userId, helmet, body, gloves, boots, belt, amulet, Some(itemId), ring2, mainHand, offHand)
      case EquipmentPart.Ring2 =>
        DbCharacter(id, userId, helmet, body, gloves, boots, belt, amulet, ring1, Some(itemId), mainHand, offHand)
    }

  def equipMainHand(itemId: Option[Int]) =
    DbCharacter(id, userId, helmet, body, gloves, boots, belt, amulet, ring1, ring2, itemId, offHand)

  def equipOffHand(itemId: Option[Int]) =
    DbCharacter(id, userId, helmet, body, gloves, boots, belt, amulet, ring1, ring2, mainHand, itemId)

  def equipWeapon(itemId: Option[Int], equipmentPart: EquipmentPart.Type) =
    equipmentPart match {
      case EquipmentPart.MainHand => this.equipMainHand(itemId)
      case EquipmentPart.OffHand => this.equipOffHand(itemId)
    }
}
