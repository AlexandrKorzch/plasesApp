package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

open class Booking {

    @SerializedName("id") var id: Long? = null
    @SerializedName("place_id") var placeId: Long? = null
    @SerializedName("bookingtime") var bookingTime: Long? = null
    @SerializedName("year") var year: Int? = null
    @SerializedName("day") var day: String? = null
    @SerializedName("month") var month: String? = null
    @SerializedName("hours") var hours: String? = null
    @SerializedName("minutes") var minutes: String? = null
    @SerializedName("peoples_count") var peoplesCount: String? = null
    @SerializedName("status") var status: Int? = null
    @SerializedName("discount") var discount: Int? = null
    @SerializedName("hall_id") var hallId: Int? = null
    @SerializedName("placeTypeId") var placeTypeId: Int? = null
    @SerializedName("order_sum") var orderSum: Int? = null

    @SerializedName("place") var place: PlaceExtended? = null

    override fun toString(): String {
        return "Booking(id=$id, placeId=$placeId, bookingTime=$bookingTime, year=$year, " +
                "day=$day, month=$month, hours=$hours, minutes=$minutes, peoplesCount=$peoplesCount, " +
                "status=$status, discount=$discount, hallId=$hallId, placeTypeId=$placeTypeId, " +
                "orderSum=$orderSum, place=$place)"
    }
}