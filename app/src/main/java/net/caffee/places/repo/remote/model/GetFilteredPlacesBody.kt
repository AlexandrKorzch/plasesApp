package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class GetFilteredPlacesBody{

    @SerializedName("lat") var lat: Int? = null
    @SerializedName("lng") var lng: Int? = null

    @SerializedName("order") var order: Int? = null
    @SerializedName("offset") var offset: Int? = 0
    @SerializedName("limit") var limit: Int? = 1000

    @SerializedName("distance") var distance: Int? = null
    @SerializedName("is_favorite") var isFavorite: Int? = null
    @SerializedName("has_delivery") var hasDelivery: Int? = null
    @SerializedName("search") var search: String? = null
    @SerializedName("city_id") var cityId: Long? = null
    @SerializedName("discount_date")var discountDate: String? = null
    @SerializedName("is_booking") var isBooking: Int? = null
    @SerializedName("has_booking") var hasBooking: Int? = null

    @SerializedName("type") var type: ArrayList<Long>? = ArrayList()
    @SerializedName("kitchen_type") var kitchenType: ArrayList<Long>? = ArrayList()

    override fun toString(): String {
        return "GetFilteredPlacesBody(lat=$lat, lng=$lng, order=$order, " +
                "offset=$offset, limit=$limit, distance=$distance, isFavorite=$isFavorite, " +
                "hasDelivery=$hasDelivery, search=$search, cityId=$cityId, " +
                "discountDate=$discountDate, " + "isBooking=$isBooking, hasBooking=$hasBooking, " +
                "type=$type, kitchenType=$kitchenType)"
    }
}