package applications

import directed.{Arc, StrictGraphDefaultImpl}

object GPS extends App {
    def askUsrInput() : (String, String) = {
        print("Ville de départ : ")
        val city1 = scala.io.StdIn.readLine()
        print("Ville d'arrivée : ")
        val city2 = scala.io.StdIn.readLine()
        (city1, city2)
    }

    println("Application GPS")

    val trajets = Utils.getTrajets()

    val villes = trajets.map(t => Set(t.from, t.to)).flatten.toSet
    val distances = trajets.map(t => (Arc(t.from, t.to), t.distance.toDouble)).toMap
    val temps = trajets.map(t => (Arc(t.from, t.to), t.temps.toDouble)).toMap

    println("Villes : ")
    villes.map(x => println("\t"+x))
    println("Trajets possibles : ")
    trajets.map(x => println("\t"+x))
    println()

    askUsrInput() match {
        case (from, to) => {
            println()
            println(s"Chemin le plus court (${from}, ${to}): ")
            println(StrictGraphDefaultImpl(villes, distances.keySet).shortestPath(distances)(from, to))

            println(s"Chemin le plus rapide (${from}, ${to}): ")
            println(StrictGraphDefaultImpl(villes, temps.keySet).shortestPath(temps)(from, to))
        }
    }
    
}
