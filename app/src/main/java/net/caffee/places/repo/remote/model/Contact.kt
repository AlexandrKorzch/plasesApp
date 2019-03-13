package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Contact {

    @SerializedName("phone") var phone: String = ""
    @SerializedName("email") var email: String = ""
}