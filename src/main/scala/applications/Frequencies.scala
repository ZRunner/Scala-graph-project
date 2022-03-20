package applications

object Frequencies extends App {
    def time[R](block: => R): R = {
        val t0 = System.nanoTime().toDouble
        val result = block    // call-by-name
        val t1 = System.nanoTime().toDouble
        println("Elapsed time: %.3f ms".format((t1 - t0)/1000000))
        result
    }

    println("hello world from frequencies")

    println(Utils.readFile().head)
}
