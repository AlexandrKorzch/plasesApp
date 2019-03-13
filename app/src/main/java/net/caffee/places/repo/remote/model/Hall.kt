package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Hall: BaseCategory() {
    @SerializedName("description") var description: String? = null
}