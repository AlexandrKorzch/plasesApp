package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

data class Advantage(
        @SerializedName("created_by") val createdBy: String? = null,
        @SerializedName("id") val id: String? = null,
        @SerializedName("title") val title: String? = null,
        @SerializedName("weight") val weight: String? = null,
        @SerializedName("updated_at") val updatedAt: String? = null,
        @SerializedName("status") val status: String? = null,
        @SerializedName("description") val description: String? = null,
        @SerializedName("image") val image: String? = null,
        @SerializedName("updated_by") val updated_by: String? = null,
        @SerializedName("created_at") val createdAt: String? = null
) {

    var photo : String? = null

}