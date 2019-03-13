package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class EditProfileBody(
        @SerializedName("city_id") var cityId: Long? = -1L,
        @SerializedName("first_name") var firstName: String = "",
        @SerializedName("last_name") var lastName: String = "") {

    @SerializedName("lang") var lang: String = "ru"
    @SerializedName("notifications") var notifications: String = ""
}