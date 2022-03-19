package undirected

/** Implementation of [[SimpleGraph]] using adjacency matrix
  * @param vs sequence of vertices in the order they are used in adjacency matrix
  * @param adjacency adjacency matrix
  * @tparam V type for vertices
  */
case class SimpleGraphMatrixImpl[V](vs : Seq[V], adjacency : IndexedSeq[IndexedSeq[Int]]) extends SimpleGraph[V] {

    /** @inheritdoc */
    lazy val vertices : Set[V] = vs.toSet

    /** @inheritdoc */
    lazy val edges : Set[Edge[V]] = {
      adjacency.flatten.zipWithIndex.map {
        case (e, i) => (e, i/adjacency.size, i%adjacency.size)
      }.filter(_._1 > 0).map {
        case (x, i, j) => Edge(vs(i), vs(j))
      }.toSet
    }

    /** @inheritdoc */
    def neighborsOf(v : V) : Option[Set[V]] = {
      vs.indexOf(v) match {
        case -1 => None
        case i => Some(adjacency(i).zipWithIndex.filter(_._1 > 0).map {
          case (e, i) => vs(i)
        }.toSet)
      }
    }

    /** @inheritdoc */
    def + (v : V) : SimpleGraphMatrixImpl[V] = {
      SimpleGraphMatrixImpl(vs :+ v,
        adjacency.map(row => row :+ 0) :+ IndexedSeq.fill(vs.size+1)(0)
      )
    }

    /** @inheritdoc */
    def - (v : V) : SimpleGraphMatrixImpl[V] = {
      vs.indexOf(v) match {
        case -1 => this
        case i => SimpleGraphMatrixImpl(vs.filterNot(_ == v),
          (adjacency.slice(0, i) :++ adjacency.slice(i+1, adjacency.size)).map(row => {
            row.slice(0, i) :++ row.slice(i+1, row.size)
          })
        )
      }
    }

    /** @inheritdoc */
    def +| (e : Edge[V]) : SimpleGraphMatrixImpl[V] = ???

    /** @inheritdoc */
    def -| (e : Edge[V]) : SimpleGraphMatrixImpl[V] = ???

    /** @inheritdoc */
    def withoutEdge : SimpleGraphMatrixImpl[V] = ???

    /** @inheritdoc */
    def withAllEdges : SimpleGraphMatrixImpl[V] = ???
  }

