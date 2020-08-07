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
      case a: Helmet => ArmorSet(Some(a), body, gloves, boots, belt, amulet, ring1, ring2)
      case a: Body => ArmorSet(helmet, Some(a), gloves, boots, belt, amulet, ring1, ring2)
      case a: Gloves => ArmorSet(helmet, body, Some(a), boots, belt, amulet, ring1, ring2)
      case a: Boots => ArmorSet(helmet, body, gloves, Some(a), belt, amulet, ring1, ring2)
      case a: Belt => ArmorSet(helmet, body, gloves, boots, Some(a), amulet, ring1, ring2)
      case a: Amulet => ArmorSet(helmet, body, gloves, boots, belt, Some(a), ring1, ring2)
      case a: Ring => ArmorSet(helmet, body, gloves, boots, belt, amulet, Some(a), ring2)
      case _ => this
    }
  }
}
