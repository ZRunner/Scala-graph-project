package applications

import directed.{Arc, StrictGraphDefaultImpl}

object GPS extends App {
    def time[R](block: => R): R = {
        val t0 = System.nanoTime().toDouble
        val result = block    // call-by-name
        val t1 = System.nanoTime().toDouble
        println("Elapsed time: %.3f ms".format((t1 - t0)/1000000))
        result
    }

    println("hello from gps")

    val trajets = Utils.getTrajets()
    val villes = trajets.map(t => Set(t.from, t.to)).flatten.toSet
    val distances = trajets.map(t => (Arc(t.from, t.to), t.distance.toDouble)).toMap
    val temps = trajets.map(t => (Arc(t.from, t.to), t.temps.toDouble)).toMap
    println("villes: "+villes)

    val g = StrictGraphDefaultImpl(villes, distances.keySet)
    println(g)

    println(g.shortestPath(distances)("Paris", "Marseille"))
}
