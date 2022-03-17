import undirected.{Edge, SimpleGraphDefaultImpl}

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
    var cyclicConnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4, 5, 6), Set(Edge(1, 2), Edge(3, 2), Edge(1, 4), Edge(1,5), Edge(3, 6), Edge(3, 5)))
    var acyclicConnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3), Set(Edge(1, 2), Edge(2, 3)))
    var cyclicNotconnectedGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4, 5, 6), Set(Edge(1, 2), Edge(3, 2), Edge(1, 4), Edge(1,5), Edge(5, 2)))
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

    println("graphe vide (attendu true): "+emptyGraph.isAcyclic)
    println("graphe cyclique connexe (attendu false): "+time{cyclicConnectedGraph.isAcyclic})
    println("graphe acyclique connexe (attendu true): "+time{acyclicConnectedGraph.isAcyclic})
    println("graphe cyclique non connexe (attendu false): "+time{cyclicNotconnectedGraph.isAcyclic})
    println("graphe acyclique non connexe (attendu true): "+acyclicNotconnectedGraph.isAcyclic)
}

