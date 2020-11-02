package game.items

import game.items.ItemType.Type

trait Armor extends Item {
  def armorPart: ArmorPart.Type

  def armor: Int
  def armorType: ArmorType.Type

  override def _type: Type = ItemType.Armor
}

object ArmorPart extends Enumeration {
  type Type = Value
  val Helmet, Body, Greaves, Belt, Amulet, Shield = Value
}

object ArmorType extends Enumeration {
  type Type = Value
  val Light, Medium, Heavy = Value
}

object Armor

case class Helmet(name: String,
                  cd: Int,
                  armor: Int,
                  armorType: ArmorType.Type,
                  rarity: Rarity.Type = Rarity.Mediocre,
                  passiveEffects: List[PassiveEffect] = List.empty,
                  activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorPart: ArmorPart.Type = ArmorPart.Helmet
}

case class Body(name: String,
                cd: Int,
                armor: Int,
                armorType: ArmorType.Type,
                rarity: Rarity.Type = Rarity.Mediocre,
                passiveEffects: List[PassiveEffect] = List.empty,
                activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorPart: ArmorPart.Type = ArmorPart.Body
}

case class Greaves(name: String,
                   cd: Int,
                   armor: Int,
                   armorType: ArmorType.Type,
                   rarity: Rarity.Type = Rarity.Mediocre,
                   passiveEffects: List[PassiveEffect] = List.empty,
                   activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorPart: ArmorPart.Type = ArmorPart.Greaves
}

case class Amulet(name: String,
                  cd: Int,
                  armor: Int,
                  armorType: ArmorType.Type,
                  rarity: Rarity.Type = Rarity.Mediocre,
                  passiveEffects: List[PassiveEffect] = List.empty,
                  activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorPart: ArmorPart.Type = ArmorPart.Amulet
}

case class Shield(name: String,
                  cd: Int,
                  armor: Int,
                  armorType: ArmorType.Type,
                  rarity: Rarity.Type = Rarity.Mediocre,
                  passiveEffects: List[PassiveEffect] = List.empty,
                  activeEffects: List[ActiveEffect] = List.empty) extends Armor {
  override def armorPart: ArmorPart.Type = ArmorPart.Shield
}
