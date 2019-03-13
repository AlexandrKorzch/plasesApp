package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Dir {
    @SerializedName("id") val id: Int = 0
    @SerializedName("title") val title: String = ""
    @SerializedName("status") val status: Int = 0
}