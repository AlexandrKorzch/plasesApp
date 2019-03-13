package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class BalanceRechargeBody(
        @SerializedName("status") val status: Int? = null,
        @SerializedName("direction") val direction: Int? = null) {

    @SerializedName("offset") var offset: Int? = 0
    @SerializedName("limit") var limit: Int? = 1000
}