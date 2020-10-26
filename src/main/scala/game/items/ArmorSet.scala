package game.items

object ArmorSet {
  def empty: ArmorSet = new ArmorSet(None, None, None, None)
}

case class ArmorSet(helmet: Option[Helmet],
                    body: Option[Body],
                    greaves: Option[Greaves],
                    amulet: Option[Amulet]) {

  def getAll: List[Armor] =
    (List.empty :+ helmet :+ body :+ greaves :+ amulet)
      .filter(_.isDefined)
      .map(_.get)
}
