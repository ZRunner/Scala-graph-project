package undirected

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

/** Trait for an undirected and ''simple'' graph, that is without loop nor parallel edges
  * @tparam V type for vertices
  */
trait SimpleGraph[V] {
    /* QUERY METHODS */

    /** The set of all vertices of the graph */
    val vertices : Set[V]

    /** The set of all    edges of the graph */
    val edges : Set[Edge[V]]

    /** The set of all vertices adjacent to input vertex
      * @param v vertex
      * @return [[None]] if `v` is not an actual vertex, the set of all neighbors of `v` otherwise (may be empty)
      */
    def neighborsOf(v : V) : Option[Set[V]]

    /** The number of adjacent vertices to input vertex
      * @param v vertex
      * @return [[None]] if `v` is not an actual vertex, the degree of `v` otherwise
      */
    def degreeOf(v : V) : Option[Int] = neighborsOf(v) map { _.size }

    /** Checks if there exists a path between two vertices
      * @param v1 one end of path to search
      * @param v2 other end of path to search
      * @return `true` if `v1` and `v2` are equal or if a path exists between `v1` and `v2`, `false` otherwise
      */
    def hasPath(v1 : V, v2 : V) : Boolean = {
      def hasPathRecursiv(start: V, end: V, visited: Set[V]): Boolean = {
        if (visited.contains(start)) false
        // else (start == end) || neighborsOf(start).map(_.map(x => hasPathRecursiv(x, end, visited+start))).map(_.exists(identity)).getOrElse(false)
        else (start == end) || neighborsOf(start).getOrElse(Set()).map(x => hasPathRecursiv(x, end, visited+start)).exists(identity)
      }
      hasPathRecursiv(v1, v2, Set())
    }

    /** Checks if graph is connected */
    // lazy val isConnected : Boolean = {
    //   def visit(start: V, visited: Set[V]) : Boolean = {
    //     if (visited == vertices) true
    //     else if (visited.contains(start)) false
    //     else neighborsOf(start).getOrElse(Set()).map(visit(_, visited+start)).exists(identity)
    //   }
    //   if (vertices.size == 0) true
    //   else visit(vertices.head, Set())
    // }

    /* Une autre méthode, un peu plus gourmande - Arthur */
    lazy val isConnected : Boolean = {
      def visit(node: V, visited: Set[V]): Set[V] = {
        if (visited == vertices || visited.contains(node)) visited
        else neighborsOf(node).getOrElse(Set()).map(visit(_, visited+node)).flatten
      }
      if (vertices.size == 0) true
      else visit(vertices.head, Set()) == vertices
    }
    

    /** Checks if graph is acyclic */
    lazy val isAcyclic: Boolean = {      
      /* vérifie si un booléen a déjà été retourné (normalement toujours false), sinon fusionne les deux listes de nœuds visités */
      def _map(x: Either[Set[V], Boolean], y: Either[Set[V], Boolean]): Either[Set[V], Boolean] = {
        // le pattern matching c'est la vie
        (x, y) match {
          case (Right(_), _)     => x
          case (_, Right(_))     => y
          case (Left(l1), Left(l2)) => Left(l1 ++ l2)
        }
      }

      /* Parcours un sous-graphe à partir d'un nœud donné
        Retourne false si un cycle est trouvé, sinon la liste des nœuds parcourus
      */
      def acyclicDFS(node: V, visited: Set[V], edges: Set[Edge[V]]): Either[Set[V], Boolean] = {
        // println("-> acyclicDFS("+node+", "+visited+", "+edges+")")
        if (visited.contains(node)) Right(false) // on a trouvé une boucle
        else vertices.filter(v => edges.contains(Edge(node, v))).map(v =>
            // on visite chaque voisin autre que le parent (on modifie le set *edges* pour éviter de retourner en arrière)
            acyclicDFS(v, visited+node, edges-Edge(node, v))
          ).reduceOption(_map).getOrElse(Left(visited+node)) // réduction spéciale si jamais aucun enfant n'a été trouvé
      }
      /* Vérifie que tous les sous-graphes soient bien parcourus
        Retourne false si un cycle est trouvé, true sinon
      */
      def acyclicHead(to_visit: Set[V]): Boolean = {
        if (to_visit.size < 3) return true // un graphe à 0, 1 ou 2 nœuds est forcément acyclique
        acyclicDFS(to_visit.head, Set(), edges) match {
          case Right(_) => false
          case Left(list) if list == vertices => true // tous les vertices ont été parcourus, end of the story
          case Left(list) => acyclicHead(to_visit -- list) // il reste des sous-graphes à parcourir
        }
      }

      acyclicHead(vertices)
    }

    /** Checks if graph is a tree */
    lazy val isTree : Boolean = isConnected && isAcyclic

    /* VERTEX OPERATIONS */

    /** Add vertex to graph
      * @param v new vertex
      * @return the graph with new vertex `v`
      *         if `v` is an actual vertex of graph, return input graph
      */
    def + (v : V) : SimpleGraph[V]

    /** Remove vertex from graph
      * @param v new vertex
      * @return the graph without vertex `v`
      *         if `v` is not an actual vertex of graph, return input graph
      */
    def - (v : V) : SimpleGraph[V]

    /* EDGE OPERATIONS */

    /** Add edge to graph (also add edge ends as new vertices if necessary)
      * @param e new edge
      * @return the graph with new edge `e`
      *         if `e` is an actual edge of graph, return input graph
      */
    def +| (e : Edge[V]) : SimpleGraph[V]

    /** Remove edge from graph (does NOT remove ends)
      * @param e new edge
      * @return the graph without edge `e`
      *         if `e` is not an actual edge of graph, return input graph
      */
    def -| (e : Edge[V]) : SimpleGraph[V]

    /** Remove all edges from graph but keep same vertices
      * @return graph with same vertices without any edge
      */
    def withoutEdge : SimpleGraph[V]

    /** Add all possible edge with same vertices
      * @return graph with same vertices and all possible edges
      */
    def withAllEdges : SimpleGraph[V]

    /* VALUATED GRAPH METHODS */

    /** Total value of the graph
      * @param valuation valuation used
      * @return total value of the graph, i.e. sum of values of all edges
      */
    def value(valuation : Map[Edge[V], Double]) : Double = (edges map { valuation(_) }).sum

    /** Minimum spanning tree
      * @param valuation valuation used
      * @return a spanning tree whose value is minimal
      */
    def minimumSpanningTree(valuation : Map[Edge[V], Double]) : SimpleGraph[V] = {
      def rec(T: SimpleGraph[V], to_check: Map[Edge[V], Double]) : SimpleGraph[V] = {
        if (T.edges.size == this.vertices.size - 1) T // terminé !
        else if (to_check.size == 0) this.withoutEdge // impossible de lier tous les poits
        else if ((T +| to_check.head._1).isAcyclic) rec(T +| to_check.head._1, to_check - to_check.head._1) // edge valide, on l'ajoute à l'arbre et on continue
        else rec(T, to_check - to_check.head._1) // edge invalide, on essaie le suivant
      }

      if (!isConnected) this.withoutEdge // un graphe non connexe ne peut donner un arbre
      else if (isAcyclic) this // un graphe connexe et acyclique est déjà un arbre
      else rec(this.withoutEdge, ListMap(valuation.toSeq.sortBy(_._2):_*))
    }

    /* COLORING METHODS */

    /** Sequence of vertices sorted by decreasing degree */
    lazy val sortedVertices : Seq[V] = {
      def visit(result: Map[V, Int], to_visit: Set[Edge[V]]) : Map[V, Int] = {
        if (to_visit.isEmpty) result
        else visit(result + (to_visit.head._1 -> (result.get(to_visit.head._1).getOrElse(0) + 1)) + (to_visit.head._2 -> (result.get(to_visit.head._2).getOrElse(0) + 1)), to_visit - to_visit.head)
      }

      ListMap(visit(vertices.map(v => (v, 0)).toMap, edges).toSeq.sortWith(_._2 > _._2):_*).keySet.toSeq
    }

    /** Proper coloring using greedy algorithm (a.k.a WELSH-POWELL) */
    lazy val greedyColoring : Map[V, Int] = {
      def rec(to_visit: Seq[V], colors: Map[V, Int]) : Map[V, Int] = {
        if (to_visit.isEmpty) colors
        else rec(
          to_visit.tail,
          colors + (to_visit.head -> {
            neighborsOf(to_visit.head).getOrElse(Set()).map(v => {colors.getOrElse(v, 0)}) match {
              case n_colors => {
                if (n_colors.size == 0) 1
                else (1 to n_colors.max).find(c => !n_colors.contains(c)).getOrElse(n_colors.max+1)
              }
            }
          })
        )
      }

      rec(sortedVertices, Map())
    }

    /** Proper coloring using DSATUR algorithm */
    lazy val coloringDSATUR : Map[V, Int] = {
      def DSAT(v: V, colors: Map[V, Int]): Int = {
        neighborsOf(v).getOrElse(Set()).map(n => {colors.getOrElse(n, 0)}) match {
          case n_colors => {
            if (n_colors.size == 0 || n_colors.max == 0) degreeOf(v).getOrElse(0)
            else n_colors.size
          }
        }
      }

      def sortByDSAT(s: Seq[V], colors: Map[V, Int]) : Seq[V] = s.sortBy(DSAT(_, colors))

      def rec(to_visit: Seq[V], colors: Map[V, Int]) : Map[V, Int] = {
        if (to_visit.isEmpty) colors
        else (colors + (to_visit.head -> {
            neighborsOf(to_visit.head).getOrElse(Set()).map(v => {colors.getOrElse(v, 0)}) match {
              case n_colors => {
                if (n_colors.size == 0) 1
                else (1 to n_colors.max).find(c => !n_colors.contains(c)).getOrElse(n_colors.max+1)
              }
            }
          })) match {
            case new_colors => rec(sortByDSAT(to_visit.tail, new_colors), new_colors)
          }
      }

      rec(sortByDSAT(sortedVertices, Map(sortedVertices.head -> 1)), Map(sortedVertices.head -> 1))
    }

    /* toString-LIKE METHODS */

    /** @inheritdoc */
    override lazy val toString : String = s"({${vertices mkString ", "}}, {${edges mkString ", "}})"

    /** Graph representation in DOT language */
    lazy val toDOTString : String = {
        "strict graph {\n" +
        "    // Edges\n" +
        (edges foldLeft "    ") { _ + _.toDOTString + "\n    " } + "\n" +
        "    // Vertices\n" +
        vertices.mkString("    ", "\n    ", "\n") +
        "  }\n"
      }

}
