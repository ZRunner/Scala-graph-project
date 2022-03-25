package directed

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

/** Trait for a directed ''and strict'' graph, i.e. without loop nor parallel arcs */
trait StrictGraph[V] {
    /* QUERY METHODS */

    /** The set of all vertices of the graph */
    val vertices : Set[V]

    /** The set of all     arcs of the graph */
    val arcs : Set[Arc[V]]

    /** The set of all vertices with arcs incoming from input vertex
      * @param v vertex
      * @return [[None]] if `v` is not an actual vertex, the set of all successors of `v` otherwise
      */
    def successorsOf(v : V) : Option[Set[V]]

    /** The number of incoming arcs to input vertex
      * @param v vertex
      * @return [[None]] if `v` is not an actual vertex, the inner degree of `v` otherwise
      */
    def inDegreeOf(v : V) : Option[Int] = {
      if (!(vertices contains v)) None
      else Some(vertices count { arcs contains Arc(_, v) } )
    }

    /** The number of outcoming arcs to input vertex
      * @param v vertex
      * @return [[None]] if `v` is not an actual vertex, the outer degree of `v` otherwise
      */
    def outDegreeOf(v : V) : Option[Int] = successorsOf(v) map { _.size }

    /** The number of adjacent vertices to input vertex
      * @param v vertex
      * @return [[None]] if `v` is not an actual vertex, the degree of `v` otherwise
      */
    def degreeOf(v : V) : Option[Int] = {
      if (!(vertices contains v)) None
      else (inDegreeOf(v) ++ outDegreeOf(v)).reduceOption(_ + _) // that's how to sum 2 Option[Int]
    }

    /* VERTEX OPERATIONS */

    /** Add vertex to graph
      * @param v new vertex
      * @return the graph with new vertex `v`
      *         if `v` is an actual vertex of graph, return input graph
      */
    def + (v : V) : StrictGraph[V]

    /** Remove vertex from graph
      * @param v new vertex
      * @return the graph without vertex `v`
      *         if `v` is not an actual vertex of graph, return input graph
      */
    def - (v : V) : StrictGraph[V]

    /* ARC OPERATIONS */

    /** Add arc to graph (also add arc ends as new vertices if necessary)
      * @param a new arc
      * @return the graph with new arc `e`
      *         if `e` is an actual arc of graph, return input graph
      */
    def +| (a : Arc[V]) : StrictGraph[V]

    /** Remove arc from graph (does NOT remove ends)
      * @param a new arc
      * @return the graph without arc `e`
      *         if `e` is not an actual arc of graph, return input graph
      */
    def -| (a : Arc[V]) : StrictGraph[V]

    /** Remove all arcs from graph but keep same vertices
      * @return graph with same vertices without any arc
      */
    def withoutArc : StrictGraph[V]

    /** Add all possible arc with same vertices
      * @return graph with same vertices and all possible arcs
      */
    def withAllArcs : StrictGraph[V]

    /* SEARCH METHODS */

    /** A topological order of the vertex set (if exists) */
    lazy val topologicalOrder : Option[Seq[V]] = {
      @tailrec def req(g: StrictGraph[V], ordered: Option[Seq[V]]) : Option[Seq[V]] = {
         // si cycle trouvé, abort
        if (!ordered.isDefined) ordered
        // si un seul nœud restant, easy
        else if (g.vertices.size == 1) Some(ordered.getOrElse(Seq()) :+ g.vertices.head)
        // on essaie de trouver un racine du graphe restant
        else g.vertices.find(g.inDegreeOf(_) == Some(0)) match {
          // si trouvé, on l'ajoute à la liste
          case Some(v) => req(g-v, Some(ordered.getOrElse(Seq()) :+ v))
          // sinon c'est une boucle
          case None => None
        }
      }

      req(this, Some(Seq()))
    }

    /* VALUATED GRAPH METHODS */

    /** Computes a shortest path between two vertices
      * @param valuation valuation of graph
      * @param start origin      of path
      * @param end   destination of path
      * @return [[None]] if there is no path from `start` to `end`, the shortest path and its valuation otherwise
      */
    def shortestPath(valuation : Map[Arc[V], Double])(start : V, end : V) : Option[(Seq[V], Double)] = {
      /* trie la liste des nœuds en fonction de leur valeur dans la map (le plus petit en premier) */
      def sortPriority(priority: Seq[V], values: Map[V, Double]) : Seq[V] = {
        priority.sortWith((a, b) => {
          values.getOrElse(a, 0.0) < values.getOrElse(b, 0.0)
        })
      }
      /* récupère la valeur d'un arc selon la map donnée à la fonction shortestPath */
      def getValue(from: V, to: V) : Double = {
        valuation.getOrElse(Arc(from, to), Double.PositiveInfinity)
      }

      /* fonction récursive qui calcule la distance minimale de chaque nœud à partir du nœud start et de la map des valeurs */
      @tailrec def rec(priority: Seq[V], values: Map[V, Double], prev: Map[V, V]) : (Map[V, Double], Map[V, V]) = {
        // println("rec("+priority.headOption+", "+values+")")
        if (priority.size == 0) (values, prev)
        else {
          successorsOf(priority.head).getOrElse(Set()).foldLeft((values, prev)){
            case ((accv, accp), v) => (
              accv + (v -> {
                if (values.getOrElse(v, Double.PositiveInfinity) > (values.getOrElse(priority.head, 0.0) + getValue(priority.head, v)))
                  (values.getOrElse(priority.head, 0.0) + getValue(priority.head, v))
                else
                  values.getOrElse(v, Double.PositiveInfinity)
              }),
              {
                if (values.getOrElse(v, Double.PositiveInfinity) > (values.getOrElse(priority.head, 0.0) + getValue(priority.head, v)))
                  accp + (v -> priority.head)
                else
                  accp
              }
            )
          } match {
            case (updated_values, updated_prev) => rec(sortPriority(priority.tail, updated_values), updated_values, updated_prev)
          }
        }
      }

      @tailrec def findPath(path: Seq[V], length: Double, prev: Map[V, V]) : Option[(Seq[V], Double)] = {
        // println("findPath("+path+", "+length+", "+values+", "+prev+")")
        if (!path.isEmpty && path.head == start) Some(path, length)
        else if (prev.isEmpty || (!path.isEmpty && !prev.contains(path.head))) None
        else prev.get(path.head) match {
          case None => None
          case Some(v) => findPath(v +: path, length+getValue(v, path.head), prev)
        }
      }

      vertices.map(v => (v, if (v==start) 0.0 else Double.PositiveInfinity)).toMap match {
        case initial_values => {
          rec(sortPriority(vertices.toSeq, initial_values), initial_values, Map()) match {
            case (_, prev) => findPath(Seq(end), 0.0, prev)
          }
        }
      }
    }

    /* toString-LIKE METHODS */

    /** @inheritdoc */
    override lazy val toString : String = s"({${vertices mkString ", "}}, {${arcs mkString ", "}})"

    /** Graph representation in DOT language */
    lazy val toDOTString : String = {
        "strict graph {\n" +
        "    // Edges\n" +
        (arcs foldLeft "    ") { _ + _.toDOTString + "\n    " } + "\n" +
        "    // Vertices\n" +
        vertices.mkString("    ", "\n    ", "\n") +
        "  }\n"
      }

  }
