object Item {
  def apply(cd: Int): Item = new Item(cd)
}

class Item(cd: Int) {
  def cd(): Int = cd
}
