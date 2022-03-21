package applications

import scala.io.Source

object Utils {
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

    def getTrajets() : Set[Trajet] = {
        Source.fromFile("src/main/resources/trajets.csv").getLines()
            .map(line => line.split(",").map(_.trim))
            .drop(1) // remove header
            .map(col => Trajet(col(0), col(1), col(2).toInt, col(3).toInt)).toSet
    }
}
