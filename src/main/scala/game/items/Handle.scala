package game.items

object HandleType extends Enumeration {
  type Type = Value
  val DualHand, TwoHand, OneHand = Value
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