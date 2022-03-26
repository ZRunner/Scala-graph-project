package applications

import scala.math.{acos, sin, cos, sqrt, pow, toRadians}

final case class GPSCoo(dg_lat: Int, mn_lat: Int, sc_lat: Int, cd_lat: Char, dg_long: Int, mn_long: Int, sc_long: Int, cd_long: Char, hauteur: Int) {

    lazy val lat : Double = {
        (dg_lat + mn_lat/60.0 + sc_lat/3600.0) * (if (cd_lat == 'S') -1 else 1)
    }

    lazy val long : Double = {
        (dg_long + mn_long/60.0 + sc_long/3600.0) * (if (cd_long == 'W') -1 else 1)
    }

    // calculate the flat distance to another GPS coordinate (not using elevation)
    def flatDistance(o: GPSCoo) : Double = {
        acos(
            sin(toRadians(o.lat)) * sin(toRadians(lat))
            + cos(toRadians(o.lat)) * cos(toRadians(lat)) * cos(toRadians(o.long-long))
        ) * 6371
    }

    def - (o : GPSCoo) : Double = {
        // use Pythagore to find the distance between 2 points using flat distance and elevation (in meters)
        sqrt(pow(flatDistance(o), 2) + pow(hauteur/1000.0-o.hauteur/1000.0, 2))
    }

    /** @inheritdoc */
    override lazy val toString : String = {
        s"GPS(${dg_lat}°${mn_lat}'${sc_lat}\"${cd_lat} ${dg_long}°${mn_long}'${sc_long}\"${cd_long} El:${hauteur})"
    }
}