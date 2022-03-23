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

    def get_values(antennes: Set[Antenne]) : Map[Edge[Antenne], Double] = {
        antennes.zipWithIndex.map{ case (a, i) => {
            antennes.slice(i+1, antennes.size).map(b => (Edge(a, b) -> (a.coo - b.coo)))
        }}.flatten.toMap
    }

    val antennes = Utils.getAntennes().filter(_.commune == "77075")
    println(s"${antennes.size} antennes lues")

    val g = SimpleGraphDefaultImpl(antennes, Set[Edge[Antenne]]())
    println("Graphe créé")
    val valuation = get_values(antennes)

    val tree = g.withAllEdges.minimumSpanningTree(valuation)
    println("Liste des antennes :")
    tree.vertices.map(x => println("\t"+x))
    println("Liaison des antennes :")
    tree.edges.map(x => println("\tAntenne "+x._1.id+" et antenne "+x._2.id+" ("+(x._1.coo-x._2.coo)+"km de distance)"))
    println("Distance totale : "+tree.value(valuation))
}
