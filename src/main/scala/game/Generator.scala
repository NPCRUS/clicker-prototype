package game

import game.items._
import game.items.Weapon
import AttributeScaling._

import scala.util.Random

object Generator {
  def generateBotEnemy(mapLevel: Int): Pawn = {
    val hp = 100 + mapLevel * 15

    val initialProperties = InitialProperties(hp)

    val weapon = randomWeapon(mapLevel)
    val handle = Handle(Some(weapon), None)
    val armorSet = ArmorSet(None, None, None, None)

    Pawn(generateBotName, handle, armorSet, initialProperties)
  }

  def generateReward(mapLevel: Int): Option[Item] = {
    if(Random.between(0, 100) > 10) None
    else Some(getListOfItems(mapLevel).random)
  }

  private def randomWeapon(mapLevel: Int): Weapon = {
    val listOfAdjective = List("hilarious", "demonic", "ubiquitous", "unreal", "masterful", "competitive", "communist", "medieval", "blunt", "rusty", "sharp", "heavy", "powerful", "black", "pink", "light", "balanced", "swiss", "huge", "great")

    getListOfWeapons(mapLevel).random
  }

  private def getListOfWeapons(level: Int): List[Weapon] = {
    List(
      updateWeapon(level, Sword("sword", 0, 0, getTwoHanded, DamageType.Physical, getRarity)),
      updateWeapon(level, Dagger("dagger", 0, 0, DamageType.Physical, getRarity)),
      updateWeapon(level, Polearm("polearm", 0, 0, getTwoHanded, DamageType.Physical, getRarity)),
      updateWeapon(level, Axe("axe", 0, 0, getTwoHanded, DamageType.Physical, getRarity))
    )
  }

  private def getListOfItems(level: Int): List[Item] = {
    val cd = calcItemCd(level)
    val rarity = getRarity
    val armorType = ArmorType.values.toList.random

    getListOfWeapons(level) ++ List(
      Helmet("helmet", cd, calcArmor(level, ArmorPart.Helmet), armorType, rarity),
      Body("body", cd, calcArmor(level, ArmorPart.Body), armorType, rarity),
      Greaves("greaves", cd, calcArmor(level, ArmorPart.Greaves), armorType, rarity),
      Amulet("amulet", cd, calcArmor(level, ArmorPart.Amulet), armorType, rarity),
      Shield("shield", cd, calcArmor(level, ArmorPart.Shield), armorType, rarity)
    )
  }

  private def updateWeapon(level: Int, weapon: Weapon): Weapon = {
    val _type = weapon.weaponType
    val preCalc = calcCdAndDamage(level, _type)
    val (cd, damage) = twoHandedConversion(weapon.twoHanded, preCalc._1, preCalc._2)

    weapon match {
      case s: Sword => s.copy(cd = cd, baseDamage = damage)
      case d: Dagger => d.copy(cd = cd, baseDamage = damage)
      case p: Polearm => p.copy(cd = cd, baseDamage = damage)
      case a: Axe => a.copy(cd = cd, baseDamage = damage)
    }
  }

  private def getTwoHanded: Boolean = {
    val rnd = Random.nextInt(100)
    if(rnd < 70) false
    else true
  }

  private def getRarity: Rarity.Type = {
    val rnd = Random.nextInt(100)
    if(rnd < 70)
      Rarity.Mediocre
    else if(rnd > 71 && rnd < 95)
      Rarity.Common
    else
      Rarity.Masterpiece
  }

  private def applyEffectsByType(item: Item, level: Int): Item = {
    if(item.rarity == Rarity.Mediocre) item
    else {
      item match {
        case w: Sword => w.copy(passiveEffects = getEffectsByWeaponType(level, w.weaponType).randomC)
        case d: Dagger => d.copy(passiveEffects = getEffectsByWeaponType(level, d.weaponType).randomC)
        case p: Polearm => p.copy(passiveEffects = getEffectsByWeaponType(level, p.weaponType).randomC)
        case a: Axe => a.copy(passiveEffects = getEffectsByWeaponType(level, a.weaponType).randomC)
        case s: Shield => s.copy(passiveEffects = getEffectsForArmor(level, s.armorType, s.armorPart).randomC)
        case h: Helmet => h.copy(passiveEffects = getEffectsForArmor(level, h.armorType, h.armorPart).randomC)
        case b: Body => b.copy(passiveEffects = getEffectsForArmor(level, b.armorType, b.armorPart).randomC)
        case g: Greaves => g.copy(passiveEffects = getEffectsForArmor(level, g.armorType, g.armorPart).randomC)
        case a: Amulet => a.copy(passiveEffects = getEffectsForArmor(level, a.armorType, a.armorPart).randomC)
      }
    }
  }

  private def generateBotName: String = {
    val firstPart = List("ugly", "pickled", "funny", "crooked", "blind", "deaf", "smart", "strong", "giant", "small", "hilarious", "soaked")
    val secondPart = List("bandit", "thug", "mercenary", "alchemist", "warrior", "viking")
    s"${firstPart.random} ${secondPart.random}"
  }

  implicit class RandomListExtension[T](list: List[T]) {
    def random: T = list(Random.nextInt(list.length))

    def randomC: List[T] = List(random)
  }
}
