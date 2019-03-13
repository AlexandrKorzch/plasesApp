package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Profile {
    @SerializedName("id") var id: Long = -1
    @SerializedName("city_id") var cityId: Long = -1
    @SerializedName("notifications") var notifications: Int = -1
    @SerializedName("wallet_balance") var walletBalance: Int = -1
    @SerializedName("bookings_count") var bookingsCount: Int = -1
    @SerializedName("phone") var phone: String = ""
    @SerializedName("image") var image: String = ""
    @SerializedName("lang") var status: String = ""
    @SerializedName("last_name") var lastName: String = ""
    @SerializedName("first_name") var firstName: String = ""
    @SerializedName("city_title") var cityTitle: String = ""
}