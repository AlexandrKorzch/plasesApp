package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

open class BaseCategory {
    @SerializedName("id") var id: Long = -1
    @SerializedName("title") var title: String = ""
    @SerializedName("image") var image: String = ""
    @SerializedName("status") var status: Int = -1
    @SerializedName("updated_at") var updatedAt: Long = 0
    @SerializedName("updated_by") var updatedBy: Long = 0
    @SerializedName("created_at") var createdAt: Long = 0
    @SerializedName("created_by") var createdBy: Long = 0
}