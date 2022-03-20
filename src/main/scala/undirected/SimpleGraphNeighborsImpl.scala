package undirected

/** Implementation of [[SimpleGraph]] using list of neighbors for each vertex
  * @param neighbors associative map providing set of neighbors for each vertex
  *                  Key must be defined for any vertex in graph : should an actual vertex have no neighbor, value is defined and is an empty set
  * @tparam V type for vertices
  */
case class SimpleGraphNeighborsImpl[V](neighbors : Map[V, Set[V]]) extends SimpleGraph[V] {

    /** @inheritdoc */
    val vertices : Set[V] = neighbors.keySet

    /** @inheritdoc */
    val edges : Set[Edge[V]] = neighbors.map{
      case (from, to) => to.map(Edge(from, _))
    }.flatten.toSet

    /** @inheritdoc */
    def neighborsOf(v: V) : Option[Set[V]] = neighbors.get(v)

    /** @inheritdoc */
    def + (v : V) : SimpleGraphNeighborsImpl[V] = {
      SimpleGraphNeighborsImpl(neighbors + (v -> Set[V]()))
    }

    /** @inheritdoc */
    def - (v : V) : SimpleGraphNeighborsImpl[V] = {
      SimpleGraphNeighborsImpl(neighbors.map{
        case (from, to) => (from, to.filterNot(_ == v))
      } - v)
    }

    /** @inheritdoc */
    def +| (e: Edge[V]) : SimpleGraphNeighborsImpl[V] = {
      if (neighbors.contains(e._1) && neighbors.contains(e._2)) {
        SimpleGraphNeighborsImpl(neighbors.map{
          case (from, to) => {
            if (from == e._1) (from, to + e._2)
            else if (from == e._2) (from, to + e._1)
            else (from, to)
          }
        })
      }
      else this
    }

    /** @inheritdoc */
    def -| (e: Edge[V]) : SimpleGraphNeighborsImpl[V] = {
      if (neighbors.contains(e._1) && neighbors.contains(e._2)) {
        SimpleGraphNeighborsImpl(neighbors.map{
          case (from, to) => {
            if (from == e._1) (from, to - e._2)
            else if (from == e._2) (from, to - e._1)
            else (from, to)
          }
        })
      }
      else this
    }

    /** @inheritdoc */
    def withoutEdge : SimpleGraphNeighborsImpl[V] = {
      SimpleGraphNeighborsImpl(neighbors.map{
        case (from, to) => (from, Set[V]())
      })
    }

    /** @inheritdoc */
    def withAllEdges : SimpleGraphNeighborsImpl[V] = {
      SimpleGraphNeighborsImpl(neighbors.map{
        case (from, to) => (from, neighbors.keySet.filterNot(_ == from))
      })
    }
}
