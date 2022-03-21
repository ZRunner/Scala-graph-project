package applications

final case class Trajet(from: Ville, to: Ville, temps: Int, distance: Int) {
    /** @inheritdoc */
    override def equals(o : Any) : Boolean = o match {
        case that : Trajet => that.from == from && that.to == to
        case _              => false
    }

    /* [hashCode] is redefined so Edge(a, b).hashCode == Edge(b, a).hashCode */
    /** @inheritdoc */
    override def hashCode : Int = from.hashCode * to.hashCode
}