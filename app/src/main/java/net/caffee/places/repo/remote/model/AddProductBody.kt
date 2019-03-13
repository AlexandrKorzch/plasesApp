package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class AddProductBody(@SerializedName("product_id") val productId: Long,
                     @SerializedName("amount") val amount: Int)

