package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class CurrentBooking : Booking() {

    @SerializedName("placeType") var placeType: PlaceType? = null

    override fun toString(): String {
        return "CurrentBooking(placeType=$placeType)"
    }
}