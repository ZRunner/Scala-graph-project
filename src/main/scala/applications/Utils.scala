package applications

import scala.io.Source

object Utils {
    def readFile() : Set[Antenne] = {
        Source.fromFile("src/main/resources/antennes.csv").getLines()
            .map(line => line.split(";").map(_.trim))
            .drop(1) // remove header
            .filter(_.size == 15) // remove incomplete line
            .map(col => Antenne(col(1),
                GPS(col(3).toInt, col(4).toInt, col(5).toInt, col(6)(0), col(7).toInt, col(8).toInt, col(9).toInt, col(10)(0), col(11).toInt),
                col(14)
                )).toSet
    }
}
