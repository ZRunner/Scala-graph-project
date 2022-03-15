import undirected.{Edge, SimpleGraphDefaultImpl}

object Main extends App {
    println("Hello, world")

    var graph = new SimpleGraphDefaultImpl[Int](Set(1, 2, 3, 4), Set(Edge(1, 2), Edge(3, 2), Edge(1, 4)))

    graph = graph + 5 + 6
    graph = graph +| Edge(4, 5)

    println(graph.hasPath(1, 2))
    println(graph.hasPath(4, 5))
    println(graph.hasPath(1, 3))
    println(graph.hasPath(1, 6))

    println(graph.neighborsOf(4))
}