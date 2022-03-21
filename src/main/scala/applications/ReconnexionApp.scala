package applications

import undirected.{Edge, SimpleGraphDefaultImpl}
import scala.collection.immutable.ListMap

object Reconnexion extends App {
    def time[R](block: => R): R = {
        val t0 = System.nanoTime().toDouble
        val result = block    // call-by-name
        val t1 = System.nanoTime().toDouble
        println("Elapsed time: %.3f ms".format((t1 - t0)/1000000))
        result
    }

    val antennes = Utils.getAntennes().filter(_.commune == "87142")
    println(s"${antennes.size} antennes lues")

    def get_values(antennes: Set[Antenne]) : Map[Edge[Antenne], Double] = {
        antennes.zipWithIndex.map{ case (a, i) => {
            antennes.slice(i+1, antennes.size).map(b => (Edge(a, b) -> (a.coo - b.coo)))
        }}.flatten.toMap
    }

    val g = SimpleGraphDefaultImpl(antennes, Set[Edge[Antenne]]())
    println("g created")
    val valuation = get_values(antennes)
    // println(valuation)

    println("isConnected: "+g.withAllEdges.isConnected)
    println("isAcyclic: "+g.withAllEdges.isAcyclic)

    val tree = g.withAllEdges.minimumSpanningTree(valuation)
    println(tree, tree.value(valuation))
}
