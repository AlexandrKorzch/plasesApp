package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName
import java.util.*

class PlaceExtended : Place() {

    @SerializedName("description") var description: String = ""
    @SerializedName("default_action") var defaultAction: Int = 0
    @SerializedName("gallery") var gallery: List<String> = ArrayList()
    @SerializedName("products") var products: List<Product> = ArrayList()

    override fun toString(): String {
        return super.toString() + "\nPlaceExtended(phone=$phone, " +
                "action_description=$action_description, cityId=$cityId, blockedAt=$blockedAt, " +
                "description=$description, updatedBy=$updatedBy, createdAt=$createdAt, " +
                "userId=$userId, createdBy=$createdBy, status=$status, defaultAction=$defaultAction, " +
                "updatedAt=$updatedAt, email=$email, blockedBy=$blockedBy, " +
                "blockedReason=$blockedReason)"
    }
}