package applications

import undirected.{Edge, SimpleGraphDefaultImpl}

object Frequencies extends App {
    def time[R](block: => R): R = {
        val t0 = System.nanoTime().toDouble
        val result = block    // call-by-name
        val t1 = System.nanoTime().toDouble
        println("Elapsed time: %.3f ms".format((t1 - t0)/1000000))
        result
    }
    def get_edges(antennes: Set[Antenne]) : Set[Edge[Antenne]] = {
        antennes.zipWithIndex.map{ case (a, i) => {
            antennes.slice(i, antennes.size).filter(b => {
                a != b && (a.coo.dg_lat - b.coo.dg_lat) < 2 && (a.coo.dg_long - b.coo.dg_long) < 2 && (a.coo - b.coo) < 1
            }).map(b => Edge(a, b))
        }}.flatten
    }

    val antennes = Utils.getAntennes().filter(_.commune == "95127")
    println(s"${antennes.size} antennes lues")

    SimpleGraphDefaultImpl(antennes, get_edges(antennes))
    .coloringDSATUR.map{
        case (antenne, couleur) => {
            println("Antenne : "+antenne.id+", Coordonnées GPS : "+antenne.coo+", Fréquence : "+(5.0 + couleur/100.0))
        }
    }
}
