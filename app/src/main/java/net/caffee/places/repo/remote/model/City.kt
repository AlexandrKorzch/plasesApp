package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class City: BaseCategory() {

    @SerializedName("lat") var lat: Double = 0.0
    @SerializedName("lng") var lng: Double = 0.0
    @SerializedName("zoom") var zoom: Int = 0

    override fun toString(): String {
        return "City(id=$id, title='$title', lat=$lat, lng=$lng, zoom=$zoom)"
    }
}