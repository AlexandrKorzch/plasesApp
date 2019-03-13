package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Order {

    @SerializedName("id") var id: Long? = null
    @SerializedName("place_id") var placeId: Long? = null
    @SerializedName("placeTypeId") var placeTypeId: Long? = null
    @SerializedName("sum") var sum: Float? = null
    @SerializedName("overall_sum") var overallSum: Float? = null
    @SerializedName("delivery_sum") var deliverySum: Float? = null
    @SerializedName("hall_id") var hallId: Int? = null
    @SerializedName("table_id") var tableid: Int? = null
    @SerializedName("name") var name: String? = null
    @SerializedName("phone") var phone: String? = null
    @SerializedName("paymethod") var Int: String? = null
    @SerializedName("message") var message: String? = null
    @SerializedName("address") var address: String? = null
    @SerializedName("cart_secret") var cartSecret: String? = null

    @SerializedName("place") var place: PlaceExtended? = null
    @SerializedName("placeType") var placeType: PlaceType? = null

    override fun toString(): String {
        return "Order(id=$id, placeId=$placeId, placeTypeId=$placeTypeId, sum=$sum, " +
                "overallSum=$overallSum, deliverySum=$deliverySum, hallId=$hallId, " +
                "tableid=$tableid, name=$name, phone=$phone, Int=$Int, message=$message, " +
                "address=$address, cartSecret=$cartSecret, place=$place, placeType=$placeType)"
    }
}
