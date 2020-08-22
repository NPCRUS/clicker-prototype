package game.items

object HandleType extends Enumeration {
  type Type = Value
  val DualHand, TwoHand, OneHand = Value
}

object Handle {
  def apply(mainHand: Option[Item], offHand: Option[Item]): Handle = {
    (mainHand, offHand) match {
      case (Some(w: Weapon), _) if w.twoHanded => TwoHandedHandle(w)
      case (Some(w1: Weapon), Some(w2: Weapon)) => DualHandle(w1, w2)
      case (Some(w: Weapon), Some(s: Shield)) => OneHandedHandle(w, Some(s))
      case (Some(w: Weapon), None) => OneHandedHandle(w, None)
    }
  }
}

trait Handle {
  def getWeapons: List[Item]
  def _type: HandleType.Type
  def getShield: Option[Shield]

  class IncompatibleHandleException extends Throwable
}

case class DualHandle(
  mainHand: Weapon,
  offHand: Weapon
) extends Handle {
  if(mainHand.twoHanded || offHand.twoHanded) throw new IncompatibleHandleException

  override def _type: HandleType.Type = HandleType.DualHand
  override def getWeapons: List[Item] = List(mainHand,offHand)
  override def getShield: Option[Shield] = None
}

case class OneHandedHandle(
  mainHand: Weapon,
  offHand: Option[Shield]
) extends Handle {
  if(mainHand.twoHanded) throw new IncompatibleHandleException

  override def _type: HandleType.Type = HandleType.OneHand
  override def getWeapons: List[Item] =
    if(offHand.isDefined) List(mainHand, offHand.get)
    else List(mainHand)

  override def getShield: Option[Shield] =
    if(offHand.isDefined) offHand
    else None
}

case class TwoHandedHandle(
  mainHand: Weapon
) extends Handle {
  if(!mainHand.twoHanded) throw new IncompatibleHandleException

  override def _type: HandleType.Type = HandleType.TwoHand
  override def getWeapons: List[Item] = List(mainHand)
  override def getShield: Option[Shield] = None
}