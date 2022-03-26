package applications

final case class Antenne(id: String, coo: GPSCoo, commune: String) {

    /** @inheritdoc */
    override def equals(o : Any) : Boolean = o match {
        case that : Antenne => that.id == id
        case _              => false
    }

    /* [hashCode] is redefined so Edge(a, b).hashCode == Edge(b, a).hashCode */
    /** @inheritdoc */
    override def hashCode : Int = id.hashCode

    /** @inheritdoc */
    override lazy val toString : String = {
        s"Antenne(${id}, GPS(${coo}), ${commune})"
    }
}