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

    /** Filtre une liste d'antennes pour supprimer celles ayant les mêmes coordonnées */
    def filterUniqueCoo(list: Set[Antenne]) : Set[Antenne] = {
        list.groupBy(_.coo).map(_._2.head).toSet
    }

    /** Récupère la distance entre deux antennes (valeur de l'arête) pour chaque couple d'antennes du graphe */
    def get_values(antennes: Set[Antenne]) : Map[Edge[Antenne], Double] = {
        antennes.zipWithIndex.map{ case (a, i) => {
            antennes.slice(i+1, antennes.size).map(b => (Edge(a, b) -> (a.coo - b.coo)))
        }}.flatten.toMap
    }

    val antennes = filterUniqueCoo(Utils.getAntennes().filter(_.commune == "62570"))
    println(s"${antennes.size} antennes lues")

    val g = SimpleGraphDefaultImpl(antennes, Set[Edge[Antenne]]())
    val valuation = get_values(antennes)
    println("Graphe créé")

    val tree = g.withAllEdges.minimumSpanningTree(valuation)
    println("Liste des antennes :")
    tree.vertices.map(x => println("\t"+x))
    println("Liaison des antennes :")
    tree.edges.map(x => println("\tAntenne "+x._1.id+" et antenne "+x._2.id+" (%.3f km de distance)".format(x._1.coo-x._2.coo)))
    println("Distance totale : "+tree.value(valuation))


    /* Affiche le nombre d'antennes ayant des coordonnés différentes pour chaque commune */
    // println()
    // Utils.getAntennes().groupBy(_.commune).map(g => (g._1 -> filterUniqueCoo(g._2).size)).filter(_._2 > 8).map(x => println("\t"+x))
}
