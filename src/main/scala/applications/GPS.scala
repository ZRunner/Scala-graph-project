package applications

import directed.{Arc, StrictGraphDefaultImpl}

object GPS extends App {
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

    println("Chemin le plus court (Paris, Marseille): ")
    println(StrictGraphDefaultImpl(villes, distances.keySet).shortestPath(distances)("Paris", "Marseille"))

    println("Chemin le plus rapide (Paris, Marseille): ")
    println(StrictGraphDefaultImpl(villes, temps.keySet).shortestPath(temps)("Paris", "Marseille"))
}
