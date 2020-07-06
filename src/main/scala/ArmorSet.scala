object ArmorSet {
  def empty: ArmorSet = new ArmorSet(None, None, None, None, None, None, None, None)
}

case class ArmorSet(
  helmet: Option[Armor],
  body: Option[Armor],
  gloves: Option[Armor],
  boots: Option[Armor],
  belt: Option[Armor],
  amulet: Option[Armor],
  ring1: Option[Armor],
  ring2: Option[Armor]
) {
  def getAll(): List[Armor] =
    (List.empty :+ helmet :+ body :+ gloves :+ boots :+ belt :+ amulet :+ ring1 :+ ring2)
    .filter(_.isDefined)
    .map(_.get)
}
