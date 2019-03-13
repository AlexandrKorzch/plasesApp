package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class GetTransactionsBody(
        @SerializedName("type") val type: Int = -1, // 1 or 2
        @SerializedName("status") val status: Int = -1, // 1 or 2
        @SerializedName("direction") val direction: Float = 1.3F,
        @SerializedName("offset") val offset: Int = 0,
        @SerializedName("limit") val limit: Int = 1000
)