package game

trait Handle {
  def getWeapons: List[Item]
}

case class DualHandle(
  mainHand: Option[Weapon with OneHanded],
  offHand: Option[Weapon with OneHanded])
extends Handle {
  override def getWeapons: List[Weapon] =
    (List.empty :+ mainHand :+ offHand)
      .filter(_.isDefined)
      .map(_.get)
}

case class ShieldHandle(
  mainHand: Option[Weapon with OneHanded],
  offHand: Option[Shield with OneHanded]
) extends Handle {
  override def getWeapons: List[Item] =
    if(mainHand.isDefined) List(mainHand.get)
    else List.empty
}

case class TwoHanHandle(
  mainHand: Option[Weapon with TwoHanded]
) extends Handle {
  override def getWeapons: List[Weapon] =
    if(mainHand.isDefined) List(mainHand.get)
    else List.empty
}