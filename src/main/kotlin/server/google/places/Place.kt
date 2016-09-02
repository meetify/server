package server.google.places

import server.Server

/**
 * Created by kr3v on 31.08.2016.
 */
class Place(var lat: Double, var lon: Double) {
    fun getNearbyPlaces(radius: Int) {
        var request = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + lat + "," + lon + "&" +
                "radius=" + radius + "&";
//                "key=" + Server.googlePlacesKey
    }
}