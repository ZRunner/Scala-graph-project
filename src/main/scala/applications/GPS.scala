package applications

import scala.math.{acos, sin, cos, toRadians}

final case class GPS(dg_lat: Int, mn_lat: Int, sc_lat: Int, cd_lat: Char, dg_long: Int, mn_long: Int, sc_long: Int, cd_long: Char, hauteur: Int) {

    lazy val lat : Double = {
        (dg_lat + mn_lat/60.0 + sc_lat/3600.0) * (if (cd_lat == 'S') -1 else 1)
    }

    lazy val long : Double = {
        (dg_long + mn_long/60.0 + sc_long/3600.0) * (if (cd_long == 'W') -1 else 1)
    }

    def - (o : GPS) : Double = {
        acos(
            sin(toRadians(o.lat)) * sin(toRadians(lat))
            + cos(toRadians(o.lat)) * cos(toRadians(lat)) * cos(toRadians(o.long-long))
            ) * 6371
    }
}