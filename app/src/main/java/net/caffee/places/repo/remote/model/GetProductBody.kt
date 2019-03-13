package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class GetProductBody(@SerializedName("product") val productId: Long)
