trait Item {
  def cd: Int
  def name: String
}

class Weapon(val cd: Int, val damage: Int, val name: String) extends Item {

}
