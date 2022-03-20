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

    var emptyGraph = new SimpleGraphDefaultImpl[Int](Set(), Set())
    var cyclicConnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4, 5, 6), Set(Edge(1, 2), Edge(3, 2), Edge(1, 4), Edge(1, 5), Edge(3, 6), Edge(3, 5), Edge(4, 5)))
    var acyclicConnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3), Set(Edge(1, 2), Edge(2, 3)))
    var cyclicNotconnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4, 5, 6), Set(Edge(1, 2), Edge(3, 2), Edge(1, 4), Edge(1, 5), Edge(5, 2)))
    var acyclicNotconnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4), Set(Edge(1, 2), Edge(2, 3)))

    // graph = graph + 5 + 6
    // graph = graph +| Edge(4, 5)

    // println(graph.hasPath(1, 2))
    // println(graph.hasPath(4, 5))
    // println(graph.hasPath(1, 3))
    // println(graph.hasPath(1, 6))

    // println(graph.neighborsOf(4))

    // println(time { cyclicConnectedGraph.isConnected } )
    // println(time { emptyGraph.isConnected } )

    // println("graphe vide (attendu true): "+emptyGraph.isAcyclic)
    // println("graphe cyclique connexe (attendu false): "+time{cyclicConnectedGraph.isAcyclic})
    // println("graphe acyclique connexe (attendu true): "+time{acyclicConnectedGraph.isAcyclic})
    // println("graphe cyclique non connexe (attendu false): "+time{cyclicNotconnectedGraph.isAcyclic})
    // println("graphe acyclique non connexe (attendu true): "+acyclicNotconnectedGraph.isAcyclic)

    // val a = cyclicConnectedGraph.withoutEdge +| Edge(1, 2)
    // println(a.isAcyclic)

    // println(cyclicConnectedGraph.minimumSpanningTree(Map(
    //     Edge(1, 5) -> 7,
    //     Edge(3, 6) -> 5,
    //     Edge(1, 2) -> 1,
    //     Edge(1, 4) -> 3,
    //     Edge(3, 2) -> 2,
    //     Edge(3, 5) -> 7
    // )))

    // println(cyclicConnectedGraph.sortedVertices)
    // println(acyclicNotconnectedGraph.sortedVertices)

    // val graph2 = StrictGraphDefaultImpl(Set(1, 2, 3, 4, 5, 6, 7, 8, 9), Set(Arc(1, 2), Arc(1, 8), Arc(2, 8), Arc(2, 3), Arc(3, 6), Arc(4, 3), Arc(4, 5), Arc(5, 6), Arc(9, 8)))

    // println(graph2.topologicalOrder)

    // val graph3 = SimpleGraphMatrixImpl(Seq('a', 'b', 'c'), IndexedSeq(IndexedSeq(0, 1, 0), IndexedSeq(1, 0, 1), IndexedSeq(0, 1, 0)))

    // println(graph3.vertices, graph3.edges)
    // println(graph3.neighborsOf('a'))

    // println(graph3 + 'd')
    // println(graph3 - 'c')
    // println(graph3 +| Edge('a', 'a') +| Edge('a', 'c'))
    // println(graph3 -| Edge('a', 'b'))

    // println(graph3.withoutEdge.adjacency)
    // println(graph3.withAllEdges)

    // val graph4 = SimpleGraphNeighborsImpl(Map('a' -> Set('a', 'c'), 'b' -> Set('c'), 'c' -> Set('a', 'b'), 'd' -> Set[Char]()))
    // println(graph4)
    // println(graph4.neighborsOf('a'))
    // println(graph4 - 'b' + 'g')
    // println(graph4 +| Edge('a', 'b'))
    // println(graph4 +| Edge('f', 'z'))
    // println(graph4 -| Edge('a', 'c'))
    // println(graph4 -| Edge('a', 'z'))
    // println(graph4.withoutEdge)
    // println(graph4.withAllEdges)

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
    // println(graph6)
    // println(graph6.shortestPath(Map(Arc(1, 2) -> 20, Arc(1, 3) -> 10, Arc(2, 4) -> 1, Arc(3, 4) -> 1, Arc(4, 5) -> 1, Arc(5, 6) -> 1))(1, 6))

    println(acyclicNotconnectedGraph.greedyColoring)

}

