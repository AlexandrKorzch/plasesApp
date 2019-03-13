package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

data class SignIn(
        @SerializedName("user_id") var userId: Float,
        @SerializedName("authkey") var authKey: String
)