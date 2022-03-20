package directed

/** Implementation of [[StrictGraph]] using adjacency matrix
  * @param vs sequence of vertices in the order they are used in adjacency matrix
  * @param adjacency adjacency matrix
  * @tparam V type for vertices
  */
case class StrictGraphMatrixImpl[V](vs : Seq[V], adjacency : IndexedSeq[IndexedSeq[Int]]) extends StrictGraph[V] {

    /** @inheritdoc */
    lazy val vertices : Set[V] = vs.toSet

    /** @inheritdoc */
    lazy val arcs : Set[Arc[V]] = {
      adjacency.flatten.zipWithIndex.map {
        case (e, i) => (e, i/adjacency.size, i%adjacency.size)
      }.filter(_._1 > 0).map {
        case (x, i, j) => Arc(vs(i), vs(j))
      }.toSet
    }

    /** @inheritdoc */
    def successorsOf(v : V) : Option[Set[V]] = {
      vs.indexOf(v) match {
        case -1 => None
        case i => Some(adjacency(i).zipWithIndex.filter(_._1 > 0).map {
          case (e, i) => vs(i)
        }.toSet)
      }
    }

    /** @inheritdoc */
    def + (v : V) : StrictGraphMatrixImpl[V] = {
      StrictGraphMatrixImpl(vs :+ v,
        adjacency.map(row => row :+ 0) :+ IndexedSeq.fill(vs.size+1)(0)
      )
    }

    /** @inheritdoc */
    def - (v : V) : StrictGraphMatrixImpl[V] = {
      vs.indexOf(v) match {
        case -1 => this
        case i => StrictGraphMatrixImpl(vs.filterNot(_ == v),
          (adjacency.slice(0, i) :++ adjacency.slice(i+1, adjacency.size)).map(row => {
            row.slice(0, i) :++ row.slice(i+1, row.size)
          })
        )
      }
    }

    /** @inheritdoc */
    def +| (e : Arc[V]) : StrictGraphMatrixImpl[V] = {
      (vs.indexOf(e._1), vs.indexOf(e._2)) match {
        case (-1, _) => this // si le 1er nœud est introuvable, rien à modifier
        case (_, -1) => this // si le 2e nœud est introuvable, rien à modifier
        case (x, y) => StrictGraphMatrixImpl(vs, adjacency.zipWithIndex.map {
          // pour chaque ligne i, chaque colonne j
          case (row, i) => { row.zipWithIndex.map {
            case (value, j) => {
              if (i==x && j==y && i==j) 2 // si i=j (boucle sur 1 nœud)
              else if (i==x && j==y) 1 // on ajoute l'arrête
              else value // on laisse la valeur de base
            }
          }}
        })
      }
    }

    /** @inheritdoc */
    def -| (e : Arc[V]) : StrictGraphMatrixImpl[V] = {
      (vs.indexOf(e._1), vs.indexOf(e._2)) match {
        case (-1, _) => this // si le 1er nœud est introuvable, rien à modifier
        case (_, -1) => this // si le 2e nœud est introuvable, rien à modifier
        case (x, y) => StrictGraphMatrixImpl(vs, adjacency.zipWithIndex.map {
          // pour chaque ligne i, chaque colonne j
          case (row, i) => { row.zipWithIndex.map {
            // on ajoute l'arrête ou on laisse la valeur de base selon les coo
            case (value, j) => if (i==x && j==y) 0 else value
          }}
        })
      }
    }

    /** @inheritdoc */
    def withoutArc : StrictGraphMatrixImpl[V] = {
      StrictGraphMatrixImpl(vs, IndexedSeq.fill(vs.size)(IndexedSeq.fill(vs.size)(0)))
    }

    /** @inheritdoc */
    def withAllArcs : StrictGraphMatrixImpl[V] = {
      StrictGraphMatrixImpl(vs,
        (0 to vs.size-1).map(i => {
          (0 to vs.size-1).map(j => if (i==j) 0 else 1).toIndexedSeq
        }).toIndexedSeq
      )
    }

}
