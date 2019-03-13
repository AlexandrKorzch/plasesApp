package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Support{

    @SerializedName("categories") val categories: List<BaseCategory>? = null
    @SerializedName("contacts") var contact: Contact? = null
}

