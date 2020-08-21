package game.items

object ArmorSet {
  def empty: ArmorSet = new ArmorSet(None, None, None, None, None, None, None, None)
}

case class ArmorSet(
  helmet: Option[Helmet],
  body: Option[Body],
  gloves: Option[Gloves],
  boots: Option[Boots],
  belt: Option[Belt],
  amulet: Option[Amulet],
  ring1: Option[Ring],
  ring2: Option[Ring]
) {
  def getAll: List[Armor] =
    (List.empty :+ helmet :+ body :+ gloves :+ boots :+ belt :+ amulet :+ ring1 :+ ring2)
    .filter(_.isDefined)
    .map(_.get)

  // TODO: make second ring wearable
  def wear(a: Armor): ArmorSet = {
    a match {
      case a: Helmet => this.copy(helmet = Some(a))
      case a: Body => this.copy(body = Some(a))
      case a: Gloves => this.copy(gloves = Some(a))
      case a: Boots => this.copy(boots = Some(a))
      case a: Belt => this.copy(belt = Some(a))
      case a: Amulet => this.copy(amulet = Some(a))
      case a: Ring => this.copy(ring1 = Some(a))
      case _ => this
    }
  }
}
