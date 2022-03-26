package applications

import undirected.{Edge, SimpleGraphDefaultImpl}

object Frequencies extends App {
    def get_edges(antennes: Set[Antenne]) : Set[Edge[Antenne]] = {
        antennes.zipWithIndex.map{ case (a, i) => {
            antennes.slice(i, antennes.size).filter(b => {
                a != b && (a.coo.dg_lat - b.coo.dg_lat) < 2 && (a.coo.dg_long - b.coo.dg_long) < 2 && (a.coo - b.coo) < 1
            }).map(b => Edge(a, b))
        }}.flatten
    }

    val antennes = Utils.getAntennes().filter(_.commune == "95127")
    println(s"${antennes.size} antennes lues")

    Utils.time {
        SimpleGraphDefaultImpl(antennes, get_edges(antennes))
        .coloringDSATUR.map{
            case (antenne, couleur) => {
                println("Antenne : "+antenne.id+", Coordonnées GPS : "+antenne.coo+", Fréquence : "+(5.0 + couleur/100.0))
            }
        }
    }
}
