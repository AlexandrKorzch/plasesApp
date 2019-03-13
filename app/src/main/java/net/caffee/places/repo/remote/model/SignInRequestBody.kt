package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

open class SignInRequestBody(
        @SerializedName("phone") val phone: String,
        @SerializedName("ostype") val osType: Int = 2) {
        @SerializedName("device_id") var deviceId: String? = null
        @SerializedName("push_id") var pushId: String? = null
        @SerializedName("code") var code: String = ""
}