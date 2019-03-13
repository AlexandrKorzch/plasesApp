package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class GetActionsBody(@SerializedName("offset") val offset: Int = 0,
                     @SerializedName("limit") val limit: Int = 1000)