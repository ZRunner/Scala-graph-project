package applications

import scala.io.Source

object Utils {
    /** Récupère la liste d'antennes du fichier antennes.csv */
    def getAntennes() : Set[Antenne] = {
        Source.fromFile("src/main/resources/antennes.csv").getLines()
            .map(line => line.split(";").map(_.trim))
            .drop(1) // remove header
            .filter(_.size == 15) // remove incomplete line
            .map(col => Antenne(col(1),
                GPSCoo(col(3).toInt, col(4).toInt, col(5).toInt, col(6)(0), col(7).toInt, col(8).toInt, col(9).toInt, col(10)(0), col(11).toInt),
                col(14)
                )).toSet
    }

    /** Filtre une liste d'antennes pour supprimer celles ayant les mêmes coordonnées */
    def filterUniqueCoo(list: Set[Antenne]) : Set[Antenne] = {
        list.groupBy(_.coo).map(_._2.head).toSet
    }

    /** Récupère la liste de trajets du fichier trajets.csv */
    def getTrajets() : Set[Trajet] = {
        Source.fromFile("src/main/resources/trajets.csv").getLines()
            .map(line => line.split(",").map(_.trim))
            .drop(1) // remove header
            .map(col => Trajet(col(0), col(1), col(2).toInt, col(3).toInt)).toSet
    }

    /** Mesure et affiche le temps passé à exécuter un bloc de code */
    def time[R](block: => R): R = {
        val t0 = System.nanoTime().toDouble
        val result = block    // call-by-name
        val t1 = System.nanoTime().toDouble
        println("Elapsed time: %.3f ms".format((t1 - t0)/1000000))
        result
    }
}
