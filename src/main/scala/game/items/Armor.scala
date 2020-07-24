package game.items
import game.items.ItemType.Type

trait Armor extends Item {
  def armorType: ArmorType.Type
  def armor: Int

  override def _type: Type = ItemType.Armor
}

object ArmorType extends Enumeration {
  type Type = Value
  val Helmet, Body, Gloves, Boots, Belt, Amulet, Ring, Shield = Value
}

case class Helmet(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Helmet
}

case class Body(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Body
}

case class Gloves(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Gloves
}

case class Boots(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Boots
}

case class Belt(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Belt
}

case class Amulet(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Amulet
}

case class Ring(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Ring
}

case class Shield(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Ring
}
