package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class GetProductsBody(
        @SerializedName("place") var placeId: Long,
        @SerializedName("category") var categoryId: Long)


