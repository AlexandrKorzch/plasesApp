package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class GetActionBody(@SerializedName("id") val promotionId: Long)