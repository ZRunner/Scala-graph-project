package applications

final case class Ville(id: Int, nom: String, coo: GPSCoo) {
    /** @inheritdoc */
    override def equals(o : Any) : Boolean = o match {
        case that : Ville => that.id == id
        case _              => false
    }

    /* [hashCode] is redefined so Edge(a, b).hashCode == Edge(b, a).hashCode */
    /** @inheritdoc */
    override def hashCode : Int = id.hashCode

    /** @inheritdoc */
    override lazy val toString : String = nom
}