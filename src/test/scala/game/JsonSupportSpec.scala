package game

import game.JsonSupport._
import game.items._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import spray.json._

// Tests here are not running one by one for some reason
// Only by running whole spec
class JsonSupportSpec extends AnyWordSpecLike with Matchers {
  val passiveEffect: PassiveEffect = PassiveEffect(EffectTargetType.Armor, 100)
  val activeEffect: OneTimeActiveEffect = OneTimeActiveEffect(
    EffectTargetType.Armor,
    0.2,
    10,
    self = true
  )
  val body: Body = Body("test", 1000, 30, ArmorType.Heavy, Rarity.Mediocre, List(passiveEffect), List(activeEffect))
  val armorSet: ArmorSet = ArmorSet.empty.copy(body = Some(body))
  val sword: Sword = Sword("test", 1000, 20, twoHanded = false, DamageType.Magical)
  val shield: Shield = Shield("test", 1, 2, ArmorType.Heavy, Rarity.Mediocre, List(passiveEffect), List(activeEffect))
  val oneHandedHandle: OneHandedHandle = OneHandedHandle(sword, Some(shield))
  val pawn: Pawn = Pawn("name", oneHandedHandle, armorSet, InitialProperties())
  val pawn2: Pawn = Pawn("name2", oneHandedHandle, armorSet, InitialProperties())

  "Effects: " should {
    "Passive effect should serialize and deserialize" in {
      val json = passiveEffect.toJson.toString
      val obj = json.parseJson.convertTo[PassiveEffect]
      obj shouldEqual passiveEffect
    }

    "OneTimeActiveEffect should serialize and deserialize" in {
      val json = activeEffect.toJson.toString
      val obj = json.parseJson.convertTo[ActiveEffect]
      obj shouldEqual activeEffect
    }

    "PeriodicActiveEffect should serialize and deserialize" in {
      val effect = PeriodicActiveEffect(
        EffectTargetType.Armor,
        0.2,
        1,
        self = true,
        2,
        100
      )
      val json = effect.toJson.toString
      val obj = json.parseJson.convertTo[ActiveEffect]
      obj shouldEqual effect
    }

    "LastingActiveEffect should serialize and deserialize" in {
      val effect = LastingActiveEffect(
        EffectTargetType.Armor,
        0.2,
        1,
        self = true,
        2000
      )
      val json = effect.toJson.toString
      val obj = json.parseJson.convertTo[ActiveEffect]
      obj shouldEqual effect
    }
  }

  "Armor:" should {
    "helmet should serialize and deserialize" in {
      val helmet = Helmet("test", 1, 2, ArmorType.Heavy, Rarity.Mediocre, List(passiveEffect), List(activeEffect))
      val json = helmet.toJson.toString
      val obj = json.parseJson.convertTo[Armor]
      obj shouldBe helmet
    }

    "body should serialize and deserialize" in {
      val body = Body("test", 1, 2, ArmorType.Heavy, Rarity.Mediocre, List(passiveEffect), List(activeEffect))
      val json = body.toJson.toString
      val obj = json.parseJson.convertTo[Armor]
      obj shouldBe body
    }

    "boots should serialize and deserialize" in {
      val greaves = Greaves("test", 1, 2, ArmorType.Heavy, Rarity.Masterpiece, List(passiveEffect), List(activeEffect))
      val json = greaves.toJson.toString
      val obj = json.parseJson.convertTo[Armor]
      obj shouldBe greaves
    }

    "amulet should serialize and deserialize" in {
      val amulet = Amulet("test", 1, 2, ArmorType.Heavy, Rarity.Masterpiece, List(passiveEffect), List(activeEffect))
      val json = amulet.toJson.toString
      val obj = json.parseJson.convertTo[Armor]
      obj shouldBe amulet
    }

    "shield should serialize and deserialize" in {
      val shield = Shield("test", 1, 2, ArmorType.Heavy, Rarity.Masterpiece, List(passiveEffect), List(activeEffect))
      val json = shield.toJson.toString
      val obj = json.parseJson.convertTo[Armor]
      obj shouldBe shield
    }
  }

  "ArmorSet should serialize and deserialize" in {
    val body = Body("test", 1000, 30, ArmorType.Heavy, Rarity.Masterpiece, List(passiveEffect), List(activeEffect))
    val armorSet = ArmorSet.empty.copy(body = Some(body))
    val json = armorSet.toJson.toString
    val obj = json.parseJson.convertTo[ArmorSet]
    obj shouldBe armorSet
  }

  "Weapon:" should {
    "sword should serialize and deserialize" in {
      val sword = Sword("test", 1000, 20, twoHanded = true)
      val json = sword.toJson.toString
      val obj = json.parseJson.convertTo[Weapon]
      obj shouldBe sword
    }

    "dagger should serialize and deserialize" in {
      val dagger = Dagger("test", 1000, 20)
      val json = dagger.toJson.toString
      val obj = json.parseJson.convertTo[Weapon]
      obj shouldBe dagger
    }

    "polearm should serialize and deserialize" in {
      val polearm = Polearm("test", 1000, 20, twoHanded = false)
      val json = polearm.toJson.toString
      val obj = json.parseJson.convertTo[Weapon]
      obj shouldBe polearm
    }

    "axe should serialize and deserialize" in {
      val axe = Axe("test", 1000, 20, twoHanded = false)
      val json = axe.toJson.toString
      val obj = json.parseJson.convertTo[Weapon]
      obj shouldBe axe
    }
  }

  "Item:" should {
    "armor should serialize and deserialize into item" in {
      val greaves = Greaves("test", 1000, 20, ArmorType.Heavy, Rarity.Masterpiece, List(passiveEffect), List(activeEffect))
      val json = greaves.toJson.toString
      val obj = json.parseJson.convertTo[Item]
      obj shouldBe greaves
    }

    "weapon should serialize and deserialize into item" in {
      val sword = Sword("test", 1000, 20, twoHanded = true, DamageType.Magical)
      val json = sword.toJson.toString
      val obj = json.parseJson.convertTo[Item]
      obj shouldBe sword
    }
  }

  "Handle:" should {
    "dualHandle should serialize and deserialize" in {
      val sword = Sword("test", 1000, 20, twoHanded = false, DamageType.Magical)
      val dagger = Dagger("test", 1000, 20)
      val dualHandle = DualHandle(sword, dagger)
      val json = dualHandle.toJson.toString
      val obj = json.parseJson.convertTo[Handle]
      obj shouldBe dualHandle
    }

    "onaHandedHandle" in {
      val sword = Sword("test", 1000, 20, twoHanded = false, DamageType.Magical)
      val shield = Shield("test", 1, 2, ArmorType.Heavy, Rarity.Masterpiece, List(passiveEffect), List(activeEffect))
      val oneHandedHandle = OneHandedHandle(sword, Some(shield))
      val json = oneHandedHandle.toJson.toString
      val obj = json.parseJson.convertTo[Handle]
      obj shouldBe oneHandedHandle
    }

    "twoHandedHandle should serialize and deserialize" in {
      val sword = Sword("test", 1000, 20, twoHanded = true, DamageType.Magical)
      val twoHandedHandle = TwoHandedHandle(sword)
      val json = twoHandedHandle.toJson.toString
      val obj = json.parseJson.convertTo[Handle]
      obj shouldBe twoHandedHandle
    }
  }

  "Pawn should serialize and deserialize" in {
    val json = pawn.toJson.toString
    val obj = json.parseJson.convertTo[Pawn]
    obj shouldBe pawn
  }

  "Opponent should serialize and deserialize" in {
    val opponent = Opponent(pawn, 100, List(activeEffect))
    val json = opponent.toJson.toString
    val obj = json.parseJson.convertTo[Opponent]
    obj shouldBe opponent
  }

  "Action:" should {
    "DamageDeal should serialize and deserialize" in {
      val opponent1 = Opponent(pawn, 100, List(activeEffect))
      val opponent2 = Opponent(pawn2, 200, List(activeEffect))
      val damageDeal = DamageDeal(opponent1, opponent2, sword, 1000, 100)

      val json = damageDeal.toJson.toString
      val obj = json.parseJson.convertTo[Action]
      obj shouldBe damageDeal
    }

    "Avoidance should serialize and deserialize" in {
      val opponent1 = Opponent(pawn, 100, List(activeEffect))
      val opponent2 = Opponent(pawn2, 200, List(activeEffect))
      val avoidance = Avoidance(opponent1, opponent2, sword, 1000, AvoidanceType.Evasion)

      val json = avoidance.toJson.toString
      val obj = json.parseJson.convertTo[Action]
      obj shouldBe avoidance
    }

    "EffectApplication should serialize and deserialize" in {
      val opponent1 = Opponent(pawn, 100, List(activeEffect))
      val opponent2 = Opponent(pawn2, 200, List(activeEffect))
      val effectApplication = EffectApplication(opponent1, opponent2, sword, 1000, activeEffect)

      val json = effectApplication.toJson.toString
      val obj = json.parseJson.convertTo[Action]
      obj shouldBe effectApplication
    }

    "EffectEnd should serialize and deserialize" in {
      val opponent1 = Opponent(pawn, 100, List(activeEffect))
      val opponent2 = Opponent(pawn2, 200, List(activeEffect))
      val effectEnd = EffectEnd(opponent1, opponent2, sword, 1000, activeEffect)

      val json = effectEnd.toJson.toString
      val obj = json.parseJson.convertTo[Action]
      obj shouldBe effectEnd
    }
  }
}
