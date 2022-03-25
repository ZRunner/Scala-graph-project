import undirected.{Edge, SimpleGraphDefaultImpl, SimpleGraphMatrixImpl, SimpleGraphNeighborsImpl}
import directed.{Arc, StrictGraphDefaultImpl, StrictGraphSuccessorsImpl}

object Main extends App {
    def time[R](block: => R): R = {
        val t0 = System.nanoTime().toDouble
        val result = block    // call-by-name
        val t1 = System.nanoTime().toDouble
        println("Elapsed time: %.3f ms".format((t1 - t0)/1000000))
        result
    }

    println("Hello, world")

    // var graph1 = new SimpleGraphDefaultImpl(Set(1, 2, 3, 4), Set(Edge(1, 2), Edge(2, 3), Edge(4, 6)))

    // graph1 = graph1 + 5 + 6
    // graph1 = graph1 +| Edge(4, 5)

    // println("graph1 chemin direct (attendu true): "+graph1.hasPath(1, 2))
    // println("graph1 chemin direct ajouté (attendu true): "+graph1.hasPath(4, 5))
    // println("graph1 chemin indirect (attendu true): "+graph1.hasPath(1, 3))
    // println("graph1 aucun chemin (attendu false): "+graph1.hasPath(1, 6))

    // println("graph1 liste de voisins (attendu {5,6}): "+graph1.neighborsOf(4))
    // println()

    // var emptyGraph = new SimpleGraphDefaultImpl[Int](Set(), Set())
    // var cyclicConnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4, 5, 6), Set(Edge(1, 2), Edge(3, 2), Edge(1, 4), Edge(1, 5), Edge(3, 6), Edge(3, 5), Edge(4, 5)))
    // var acyclicConnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3), Set(Edge(1, 2), Edge(2, 3)))
    // var cyclicNotconnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4, 5, 6), Set(Edge(1, 2), Edge(3, 2), Edge(1, 4), Edge(1, 5), Edge(5, 2)))
    // var acyclicNotconnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4), Set(Edge(1, 2), Edge(2, 3)))

    // print("graphe cyclique connexe - temps d'exécution de .isConnected:")
    // println(time { cyclicConnectedGraph.isConnected } )
    // print("graphe vide - temps d'exécution de .isConnected: ")
    // println(time { emptyGraph.isConnected } )

    // println("graphe vide (attendu true): "+emptyGraph.isAcyclic)
    // println("graphe cyclique connexe (attendu false): "+time{cyclicConnectedGraph.isAcyclic})
    // println("graphe acyclique connexe (attendu true): "+time{acyclicConnectedGraph.isAcyclic})
    // println("graphe cyclique non connexe (attendu false): "+time{cyclicNotconnectedGraph.isAcyclic})
    // println("graphe acyclique non connexe (attendu true): "+acyclicNotconnectedGraph.isAcyclic)
    // println()

    // println("graphe cyclique connexe - minimumSpanningTree: "+cyclicConnectedGraph.minimumSpanningTree(Map(
    //     Edge(1, 5) -> 7,
    //     Edge(3, 6) -> 5,
    //     Edge(1, 2) -> 1,
    //     Edge(1, 4) -> 3,
    //     Edge(3, 2) -> 2,
    //     Edge(3, 5) -> 7
    // )))

    // println("graphe cyclique connexe - sortedVertices: "+cyclicConnectedGraph.sortedVertices)
    // println("graphe acyclique non connexe - sortedVertices: "+acyclicNotconnectedGraph.sortedVertices)
    // println()

    // val graph2 = StrictGraphDefaultImpl(Set(1, 2, 3, 4, 5, 6, 7, 8, 9), Set(Arc(1, 2), Arc(1, 8), Arc(2, 8), Arc(2, 3), Arc(3, 6), Arc(4, 3), Arc(4, 5), Arc(5, 6), Arc(9, 8)))
    // println("graphe orienté - ordre topologique: "+graph2.topologicalOrder)

    // val graph3 = SimpleGraphMatrixImpl(Seq('a', 'b', 'c'), IndexedSeq(IndexedSeq(0, 1, 0), IndexedSeq(1, 0, 1), IndexedSeq(0, 1, 0)))
    // println("graphe construit depuis une matrice: "+(graph3.vertices, graph3.edges))
    // println("graphe matrice - détection de voisins (attendu {b}): "+graph3.neighborsOf('a'))

    // println("graphe matrice - ajout du nœud d: "+(graph3 + 'd'))
    // println("graphe matrice - retrait du nœud d: "+(graph3 - 'c'))
    // println("graphe matrice - ajout des arcs a->a a->c: "+(graph3 +| Edge('a', 'a') +| Edge('a', 'c')))
    // println("graphe matrice - retrait de l'arc a->b: "+(graph3 -| Edge('a', 'b')))

    // println("graphe matrice vide - matrice d'adjacence (attendu matrice nulle): "+graph3.withoutEdge.adjacency)
    // println("graphe matrice - avec tous les arcs: "+graph3.withAllEdges)
    // println()

    // val graph4 = SimpleGraphNeighborsImpl(Map('a' -> Set('a', 'c'), 'b' -> Set('c'), 'c' -> Set('a', 'b'), 'd' -> Set[Char]()))
    // println("graphe par voisins: "+graph4)
    // println("graphe par voisins - détection des voisins de a (attendu {a,c}): "+graph4.neighborsOf('a'))
    // println("graphe par voisins - retrait des nœuds b et g (g non existant): "+(graph4 - 'b' + 'g'))
    // println("graphe par voisins - ajout de l'arête a-b: "+(graph4 +| Edge('a', 'b')))
    // println("graphe par voisins - ajout de l'arête f-z (z non existant): "+(graph4 +| Edge('f', 'z')))
    // println("graphe par voisins - retrait de l'arête a-c: "+(graph4 -| Edge('a', 'c')))
    // println("graphe par voisins - retrait de l'arête a-z (z non existant): "+(graph4 -| Edge('a', 'z')))
    // println("graphe par voisins sans arête: "+graph4.withoutEdge)
    // println("graphe par voisins avec toutes les arêtes: "+graph4.withAllEdges)
    // println()

    // val graph5 = StrictGraphSuccessorsImpl(Map('a' -> Set('c'), 'b' -> Set('c'), 'c' -> Set[Char](), 'd' -> Set('a')))
    // println(graph5)
    // println(graph5.successorsOf('a'))
    // println(graph5 - 'b' + 'g')
    // println(graph5 +| Arc('a', 'b'))
    // println(graph5 +| Arc('f', 'z'))
    // println(graph5 -| Arc('a', 'c'))
    // println(graph5 -| Arc('a', 'z'))
    // println(graph5.withoutArc)
    // println(graph5.withAllArcs)
    // println(graph5.shortestPath(Map(Arc('a', 'c') -> 1, Arc('b', 'c') -> 1, Arc('d', 'a') -> 2))('d', 'c'))

    // val graph6 = StrictGraphSuccessorsImpl(Map(1 -> Set(2, 3), 2 -> Set(4), 3 -> Set(4), 4 -> Set(5), 5 -> Set(6)))
    // println("graphe orienté par successeur: "+graph6)
    // println("graphe orienté - plus court chemin 1->6 (attendu 1-3-4-5-6 pour 13.0): "+graph6.shortestPath(Map(Arc(1, 2) -> 20, Arc(1, 3) -> 10, Arc(2, 4) -> 1, Arc(3, 4) -> 1, Arc(4, 5) -> 1, Arc(5, 6) -> 1))(1, 6))

    // println(cyclicConnectedGraph.coloringDSATUR)

    // val graph7 = new SimpleGraphDefaultImpl((1 to 6).toSet, Set(Edge(1, 2), Edge(1, 3), Edge(1, 6), Edge(2, 3), Edge(3, 4), Edge(3, 5), Edge(4, 5), Edge(5, 6)))
    // println(graph7.greedyColoring)
    // println(graph7.coloringDSATUR)

}

