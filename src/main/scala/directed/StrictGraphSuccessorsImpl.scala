package directed

/** Implementation of [[StrictGraph]] using list of successors for each vertex
  * @param successors associative map providing set of successors for each vertex
  *                   Key must be defined for any vertex in graph : should an actual vertex have no neighbor, value is defined and is an empty set
  * @tparam V type for vertices
  */
case class StrictGraphSuccessorsImpl[V](successors : Map[V, Set[V]]) extends StrictGraph[V] {

    /** @inheritdoc */
    val vertices : Set[V] = successors.keySet

    /** @inheritdoc */
    val arcs : Set[Arc[V]] = successors.map{
      case (from, to) => to.map(Arc(from, _))
    }.flatten.toSet

    /** @inheritdoc */
    def successorsOf(v: V) : Option[Set[V]] = successors.get(v)

    /** @inheritdoc */
    def + (v : V) : StrictGraphSuccessorsImpl[V] = {
      StrictGraphSuccessorsImpl(successors + (v -> Set[V]()))
    }

    /** @inheritdoc */
    def - (v : V) : StrictGraphSuccessorsImpl[V] = {
      StrictGraphSuccessorsImpl(successors.map{
        case (from, to) => (from, to.filterNot(_ == v))
      } - v)
    }

    /** @inheritdoc */
    def +| (e: Arc[V]) : StrictGraphSuccessorsImpl[V] = {
      if (successors.contains(e._1) && successors.contains(e._2)) {
        StrictGraphSuccessorsImpl(successors.map{
          case (from, to) => {
            if (from == e._1) (from, to + e._2)
            else (from, to)
          }
        })
      }
      else this
    }

    /** @inheritdoc */
    def -| (e: Arc[V]) : StrictGraphSuccessorsImpl[V] = {
      if (successors.contains(e._1) && successors.contains(e._2)) {
        StrictGraphSuccessorsImpl(successors.map{
          case (from, to) => {
            if (from == e._1) (from, to - e._2)
            else (from, to)
          }
        })
      }
      else this
    }

    /** @inheritdoc */
    def withoutArc : StrictGraphSuccessorsImpl[V] = {
      StrictGraphSuccessorsImpl(successors.map{
        case (from, to) => (from, Set[V]())
      })
    }

    /** @inheritdoc */
    def withAllArcs : StrictGraphSuccessorsImpl[V] = {
      StrictGraphSuccessorsImpl(successors.map{
        case (from, to) => (from, successors.keySet.filterNot(_ == from))
      })
    }
  }

