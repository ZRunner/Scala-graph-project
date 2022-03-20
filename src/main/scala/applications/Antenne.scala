package applications

final case class Antenne(id: String, coo: GPS, commune: String) {

     /** @inheritdoc */
    override def equals(o : Any) : Boolean = o match {
        case that : Antenne => that.id == id
        case _              => false
    }

    /* [hashCode] is redefined so Edge(a, b).hashCode == Edge(b, a).hashCode */
    /** @inheritdoc */
    override def hashCode : Int = id.hashCode
}