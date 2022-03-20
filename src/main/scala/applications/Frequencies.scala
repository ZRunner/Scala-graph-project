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

    println("hello world from frequencies")

    val antennes = Utils.readFile()
    println(s"${antennes.size} antennes lues")

    def get_edges(antennes: Set[Antenne]) : Set[Edge[Antenne]] = {
        antennes.zipWithIndex.map{ case (a, i) => {
            antennes.slice(i, antennes.size).filter(b => {
                a != b && (a.coo.dg_lat - b.coo.dg_lat) < 2 && (a.coo.dg_long - b.coo.dg_long) < 2 && (a.coo - b.coo) < 2
            }).map(b => Edge(a, b))
        }}.flatten
    }

    // println(time { get_edges(antennes.slice(0, 10_000)).size } )

    val g = SimpleGraphDefaultImpl(antennes.slice(0, 6_000), get_edges(antennes.slice(0, 6_000)))
    println("g is created")
    println(time {g.coloringDSATUR} )
}
