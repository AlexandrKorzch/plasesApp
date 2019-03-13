package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class GetProductsCategoriesBody(
        @SerializedName("place") var placeId: Long
)