package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Cart {
    @SerializedName("place_id") var placeId: Long = 0
    @SerializedName("sumOverall") var totalSum: Float = 0F
    @SerializedName("delivery_sum") var deliverySum: Float = 0F
}