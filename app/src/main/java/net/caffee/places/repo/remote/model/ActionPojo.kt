package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class ActionPojo: BaseCategory() {

    @SerializedName("weight") var weight: Long = -1
    @SerializedName("enddate") var endDate: Long = -1
    @SerializedName("place_id") var placeId: Long = -1
    @SerializedName("startdate") var startDate: Long = -1
    @SerializedName("address") var address: String = ""
    @SerializedName("description") var description: String = ""
    @SerializedName("short_description") var shortDescription: String = ""

//    private val place: Place = null

    override fun toString(): String {
        return "ActionPojo(weight=$weight, endDate=$endDate, placeId=$placeId, " +
                "startDate=$startDate, address='$address', description='$description', " +
                "shortDescription='$shortDescription')"
    }
}