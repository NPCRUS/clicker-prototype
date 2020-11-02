package models

import slick.dbio.Effect
import slick.lifted.ProvenShape
import slick.sql.{FixedSqlAction, SqlAction}
import utils.MyPostgresProfile.api._

object CharacterModel extends TableQuery(new Character(_)) {

  def getCharacter(user: User): SqlAction[DbCharacter, NoStream, Effect.Read] =
    this.filter(_.userId === user.id).result.head

  def create(userId: Int): FixedSqlAction[Int, NoStream, Effect.Write] =
    this += DbCharacter(0, userId, None, None, None, None, None, None)

  def upsert(character: DbCharacter): FixedSqlAction[Int, NoStream, Effect.Write] = {
    this.insertOrUpdate(character)
  }
}

class Character(tag: Tag) extends Table[DbCharacter](tag, "character") {
  def * : ProvenShape[DbCharacter] =
    (id, userId, helmet.?, body.?, greaves.?, amulet.?, mainHand.?, offHand.?)
      .mapTo[DbCharacter]

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def helmet = column[Int]("helmet")

  def body = column[Int]("body")

  def greaves = column[Int]("greaves")

  def amulet = column[Int]("amulet")

  def mainHand = column[Int]("main_hand")

  def offHand = column[Int]("off_hand")

  def user = foreignKey("character_fk", userId, UserModel)(_.id, onUpdate = ForeignKeyAction.Cascade)

  def userId = column[Int]("user_id")
}

case class DbCharacter(id: Int, userId: Int, helmet: Option[Int], body: Option[Int], greaves: Option[Int], amulet: Option[Int], mainHand: Option[Int], offHand: Option[Int]) {
  def getIds: Seq[Int] =
    Seq(helmet, body, greaves, amulet, mainHand, offHand)
      .filter(_.isDefined)
      .map(_.get)

  def equipArmor(itemId: Option[Int], equipmentPart: EquipmentPart.Type): DbCharacter =
    equipmentPart match {
      case EquipmentPart.Helmet =>
        this.copy(helmet = itemId)
      case EquipmentPart.Body =>
        this.copy(body = itemId)
      case EquipmentPart.Greaves =>
        this.copy(greaves = itemId)
      case EquipmentPart.Amulet =>
        this.copy(amulet = itemId)
    }

  def equipWeapon(itemId: Option[Int], equipmentPart: EquipmentPart.Type): DbCharacter =
    equipmentPart match {
      case EquipmentPart.MainHand => this.equipMainHand(itemId)
      case EquipmentPart.OffHand => this.equipOffHand(itemId)
    }

  def equipMainHand(itemId: Option[Int]): DbCharacter =
    this.copy(mainHand = itemId)

  def equipOffHand(itemId: Option[Int]): DbCharacter =
    this.copy(offHand = itemId)
}
