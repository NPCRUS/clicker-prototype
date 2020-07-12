package game

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
}
