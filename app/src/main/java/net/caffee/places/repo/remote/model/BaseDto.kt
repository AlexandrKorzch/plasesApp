package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class BaseDto<DATA> {

    @SerializedName("status")
    var status : String? = null

    @SerializedName("error_message")
    var errorMessage : String? = null

    @SerializedName("error_code")
    var errorCode : Int? = 0

    @SerializedName("data")
    var data : DATA? = null
}