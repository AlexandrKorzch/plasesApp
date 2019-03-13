package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class AddOrderBody(@SerializedName("place_id") var placeId: Long = -1,
                   @SerializedName("paymethod") var payType: Int = -1,
                   @SerializedName("city") var cityId: Long = -1,
                   @SerializedName("name") var name: String = "",
                   @SerializedName("phone") var phone: String = "",
                   @SerializedName("address") var address: String = "")