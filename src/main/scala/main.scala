import undirected.{Edge, SimpleGraphDefaultImpl}

object Main extends App {
    def time[R](block: => R): R = {
        val t0 = System.nanoTime()
        val result = block    // call-by-name
        val t1 = System.nanoTime()
        println("Elapsed time: " + (t1 - t0)/1000000 + "ms")
        result
    }

    println("Hello, world")

    var graph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), Set(Edge(1, 2), Edge(3, 2), Edge(1, 4), Edge(1, 7), Edge(2, 9), Edge(3, 6), Edge(6, 8), Edge(9, 8)))
    var emptyGraph = new SimpleGraphDefaultImpl[Int](Set(), Set())
    var acyclicGraph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3), Set(Edge(1, 2), Edge(2, 3)))

    // graph = graph + 5 + 6
    // graph = graph +| Edge(4, 5)

    // println(graph.hasPath(1, 2))
    // println(graph.hasPath(4, 5))
    // println(graph.hasPath(1, 3))
    // println(graph.hasPath(1, 6))

    // println(graph.neighborsOf(4))

    // println(time { graph.isConnected } )
    // println(time { emptyGraph.isConnected } )

    println("graphe cyclique (expected false): "+graph.isAcyclic)
    println("graphe acyclique (expected true): "+acyclicGraph.isAcyclic)
    println("graphe vide (expected true): "+emptyGraph.isAcyclic)
}

