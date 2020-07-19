package game

import game.ArmorType.ArmorType

trait Armor extends Item {
  def armorType: ArmorType.ArmorType
  def armor: Int

  override def _type: String = "armor"
}

object ArmorType extends Enumeration {
  type ArmorType = Value
  val Helmet, Body, Gloves, Boots, Belt, Amulet, Ring, Shield = Value
}

case class Helmet(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType = ArmorType.Helmet
}

case class Body(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType = ArmorType.Body
}

case class Gloves(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType = ArmorType.Gloves
}

case class Boots(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType = ArmorType.Boots
}

case class Belt(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType = ArmorType.Belt
}

case class Amulet(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType = ArmorType.Amulet
}

case class Ring(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType = ArmorType.Ring
}

case class Shield(
  name: String,
  cd: Int,
  armor: Int,
  passiveEffects: List[PassiveEffect] = List.empty,
  activeEffects: List[ActiveEffect] = List.empty
) extends Armor {
  override def armorType: ArmorType = ArmorType.Ring
}
