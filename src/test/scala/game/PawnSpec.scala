package game

import game.items.{ArmorSet, ArmorType, EffectTargetType, Helmet, OneHandedHandle, OneTimeActiveEffect, PassiveEffect, PeriodicActiveEffect, Shield, Sword, TwoHandedHandle}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class PawnSpec
  extends AnyWordSpecLike
    with Matchers {

  private val sword = Sword(
    name = "test sword",
    cd = 1000,
    baseDamage = 1000,
    twoHanded = true,
    passiveEffects = List(PassiveEffect(EffectTargetType.Parry, 20), PassiveEffect(EffectTargetType.Accuracy, 50)),
    activeEffects = List(OneTimeActiveEffect(EffectTargetType.Armor, 0.2, 10, self = false))
  )
  private val handle = TwoHandedHandle(sword)

  private val helmet = Helmet(
    name = "test helmet",
    cd = 1000,
    armor = 30,
    armorType = ArmorType.Heavy,
    passiveEffects = List(
      PassiveEffect(EffectTargetType.Hp, 52),
      PassiveEffect(EffectTargetType.Armor, 32),
      PassiveEffect(EffectTargetType.Parry, 52),
      PassiveEffect(EffectTargetType.Evasion, 27),
      PassiveEffect(EffectTargetType.Block, 61),
      PassiveEffect(EffectTargetType.MagicResistance, 48),
      PassiveEffect(EffectTargetType.PhysicMastery, 94),
      PassiveEffect(EffectTargetType.MagicMastery, 29),
      PassiveEffect(EffectTargetType.CritChance, 33),
      PassiveEffect(EffectTargetType.CritMultiplier, 42),
      PassiveEffect(EffectTargetType.Accuracy, 71)
    ),
    activeEffects = List(PeriodicActiveEffect(EffectTargetType.Hp, 0.2, self = true, change = 15, ticks = 3, tickCd = 4000))
  )
  private val armorSet = ArmorSet.empty.copy(helmet = Some(helmet))

  private val initialProperties = InitialProperties(132, 53, 26, 48, 52, 31, 87, 63, 53, 48)

  private val pawn = Pawn("test_pawn", handle, armorSet, initialProperties)

  "Pawn" should {
    "allPassiveEffects should return passiveEffects from all items" in {
      pawn.allPassiveEffects should contain theSameElementsAs {
        sword.passiveEffects ++ helmet.passiveEffects
      }
    }

    "calculating hp check" in {
      pawn.hp shouldEqual initialProperties.hp + pawn.getEffectChangeByType(EffectTargetType.Hp)
    }

    "calculating armor check" in {
      val shield = Shield("", 1, 132, armorType = ArmorType.Heavy)
      val pawnWithShield = pawn.copy(handle = OneHandedHandle(Sword("", 1, 1, twoHanded = false), Some(shield)))
      pawn.armor shouldEqual armorSet.getAll.map(_.armor).sum + pawnWithShield.getEffectChangeByType(EffectTargetType.Armor)
    }

    "calculating parryRate check" in {
      pawn.parryRate shouldEqual initialProperties.parryRate + pawn.getEffectChangeByType(EffectTargetType.Parry)
    }

    "calculating evasionRate check" in {
      pawn.evasionRate shouldEqual initialProperties.evasionRate + pawn.getEffectChangeByType(EffectTargetType.Evasion)
    }

    "calculating blockRate check" in {
      pawn.blockRate shouldEqual initialProperties.blockRate + pawn.getEffectChangeByType(EffectTargetType.Block)
    }

    "calculating magicalResistance check" in {
      pawn.magicalResistance shouldEqual initialProperties.magicalResistance + pawn.getEffectChangeByType(EffectTargetType.MagicResistance)
    }

    "calculating physicalMastery check" in {
      pawn.physicalMastery shouldEqual initialProperties.physicalMastery + pawn.getEffectChangeByType(EffectTargetType.PhysicMastery)
    }

    "calculating magicalMastery check" in {
      pawn.magicalMastery shouldEqual initialProperties.magicalMastery + pawn.getEffectChangeByType(EffectTargetType.MagicMastery)
    }

    "calculating criticalChance check" in {
      pawn.criticalChance shouldEqual initialProperties.criticalChance + pawn.getEffectChangeByType(EffectTargetType.CritChance)
    }

    "calculating criticalValue check" in {
      pawn.criticalMultiplier shouldEqual initialProperties.criticalMultiplier + pawn.getEffectChangeByType(EffectTargetType.CritMultiplier)
    }

    "calculating accuracyRating check" in {
      pawn.accuracyRating shouldEqual initialProperties.accuracyRating + pawn.getEffectChangeByType(EffectTargetType.Accuracy)
    }

    "get all items should return all items" in  {
      pawn.getAllItems should contain theSameElementsAs (handle.getWeapons ++ armorSet.getAll)
    }
  }
}
