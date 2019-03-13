package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Fags {
    @SerializedName("categories") var fags: List<Fag> = ArrayList()
    @SerializedName("contacts") var contact: Contact? = null
}