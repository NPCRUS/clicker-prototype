package models
import slick.dbio.Effect
import slick.lifted.ProvenShape
import slick.sql.{FixedSqlAction, SqlAction}
import util.MyPostgresProfile.api._

object CharacterModel extends TableQuery(new Character(_)){

  def getCharacter(user: User): SqlAction[DbCharacter, NoStream, Effect.Read] =
    this.filter(_.userId === user.id).result.head

  def create(userId: Int): FixedSqlAction[Int, NoStream, Effect.Write] =
    this += DbCharacter(0, userId, None, None, None, None, None, None, None, None, None, None)

  def upsert(character: DbCharacter): FixedSqlAction[Int, NoStream, Effect.Write] = {
    this.insertOrUpdate(character)
  }
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

  def * : ProvenShape[DbCharacter] =
    (id, userId, helmet.?, body.?, gloves.?, boots.?, belt.?, amulet.?, ring1.?, ring2.?, mainHand.?, offHand.?)
      .mapTo[DbCharacter]

  def user = foreignKey("character_fk", userId, UserModel)(_.id, onUpdate = ForeignKeyAction.Cascade)
}

case class DbCharacter(
  id: Int,
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

  def equipArmor(itemId: Option[Int], equipmentPart: EquipmentPart.Type): DbCharacter =
    equipmentPart match {
      case EquipmentPart.Helmet =>
        this.copy(helmet = itemId)
      case EquipmentPart.Body =>
        this.copy(body = itemId)
      case EquipmentPart.Gloves =>
        this.copy(gloves = itemId)
      case EquipmentPart.Boots =>
        this.copy(boots = itemId)
      case EquipmentPart.Belt =>
        this.copy(belt = itemId)
      case EquipmentPart.Amulet =>
        this.copy(amulet = itemId)
      case EquipmentPart.Ring1 =>
        this.copy(ring1 = itemId)
      case EquipmentPart.Ring2 =>
        this.copy(ring2 = itemId)
    }

  def equipMainHand(itemId: Option[Int]): DbCharacter =
    this.copy(mainHand = itemId)

  def equipOffHand(itemId: Option[Int]): DbCharacter =
    this.copy(offHand = itemId)

  def equipWeapon(itemId: Option[Int], equipmentPart: EquipmentPart.Type): DbCharacter =
    equipmentPart match {
      case EquipmentPart.MainHand => this.equipMainHand(itemId)
      case EquipmentPart.OffHand => this.equipOffHand(itemId)
    }
}
