package game.items

import game.items.ItemType.Type

trait Armor extends Item {
  def armorType: ArmorType.Type

  def armor: Int

  override def _type: Type = ItemType.Armor
}

object ArmorType extends Enumeration {
  type Type = Value
  val Helmet, Body, Greaves, Belt, Amulet, Shield = Value
}

case class Helmet(name: String,
                  cd: Int,
                  armor: Int,
                  rarity: Rarity.Type = Rarity.Mediocre,
                  passiveEffects: List[PassiveEffect] = List.empty,
                  activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Helmet
}

case class Body(name: String,
                cd: Int,
                armor: Int,
                rarity: Rarity.Type = Rarity.Mediocre,
                passiveEffects: List[PassiveEffect] = List.empty,
                activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Body
}

case class Greaves(name: String,
                   cd: Int,
                   armor: Int,
                   rarity: Rarity.Type = Rarity.Mediocre,
                   passiveEffects: List[PassiveEffect] = List.empty,
                   activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Greaves
}

case class Amulet(name: String,
                  cd: Int,
                  armor: Int,
                  rarity: Rarity.Type = Rarity.Mediocre,
                  passiveEffects: List[PassiveEffect] = List.empty,
                  activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Amulet
}

case class Shield(name: String,
                  cd: Int,
                  armor: Int,
                  rarity: Rarity.Type = Rarity.Mediocre,
                  passiveEffects: List[PassiveEffect] = List.empty,
                  activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorType: ArmorType.Type = ArmorType.Shield
}
