package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class AddBookingBody(
        @SerializedName("place_id") val placeId : Long,
        @SerializedName("hall_id") var hallId : Long,
        @SerializedName("peoples_count") var peoplesCount : Int,
        @SerializedName("day") var day : Int,
        @SerializedName("month") var month : Int,
        @SerializedName("year") var year : Int,
        @SerializedName("hours") var hours : Int,
        @SerializedName("minutes") var minutes : Int)