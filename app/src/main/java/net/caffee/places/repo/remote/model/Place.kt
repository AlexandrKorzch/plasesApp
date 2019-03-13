package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

open class Place{

    @SerializedName("type") var type: String = ""
    @SerializedName("title") var title: String = ""
    @SerializedName("image") var image: String = ""
    @SerializedName("phone") var phone: String = ""
    @SerializedName("email") var email: String = ""
    @SerializedName("address") var address: String = ""
    @SerializedName("discount") var discount: String = ""
    @SerializedName("schedule") var schedule: String = ""
    @SerializedName("sub_title") var subTitle: String = ""
    @SerializedName("blocked_reason") var blockedReason: String = ""
    @SerializedName("action_description") var action_description: String = ""
    @SerializedName("status") var status: Int = 0
    @SerializedName("bookings") var bookings: Int = 0
    @SerializedName("deliveries") var deliveries: Int = 0
    @SerializedName("id") var id: Long = 0
    @SerializedName("city_id") var cityId: Long = 0
    @SerializedName("user_id") var userId: Long = 0
    @SerializedName("blocked_at") var blockedAt: Long = 0
    @SerializedName("updated_by") var updatedBy: Long = 0
    @SerializedName("created_at") var createdAt: Long = 0
    @SerializedName("updated_at") var updatedAt: Long = 0
    @SerializedName("blocked_by") var blockedBy: Long = 0
    @SerializedName("created_by") var createdBy: Long = 0
    @SerializedName("latitude") var latitude: Double = 0.0
    @SerializedName("longitude") var longitude: Double = 0.0
    @SerializedName("rating") var rating: Float = 0.0f
    @SerializedName("mp_rating") var mpRating: Float = 0.0f
    @SerializedName("action_discount") var actionDiscount: Float = 0.0f
    @SerializedName("is_booking") var isBooking: Boolean = false
    @SerializedName("is_favorite") var isFavorite: Boolean = true
    @SerializedName("has_booking") var hasBooking: Boolean = false
    @SerializedName("is_delivery") var isDelivery: Boolean = false
    @SerializedName("has_delivery") var hasDelivery: Boolean = false

    override fun toString(): String {
        return "Place(type='$type', title='$title', image='$image', phone='$phone', email='$email'" +
                ", address='$address', discount='$discount', schedule='$schedule', " +
                "subTitle='$subTitle', blockedReason='$blockedReason', " +
                "action_description='$action_description', status=$status, " +
                "bookings=$bookings, deliveries=$deliveries, id=$id, cityId=$cityId, " +
                "userId=$userId, blockedAt=$blockedAt, updatedBy=$updatedBy, " +
                "createdAt=$createdAt, updatedAt=$updatedAt, blockedBy=$blockedBy, " +
                "createdBy=$createdBy, latitude=$latitude, longitude=$longitude, rating=$rating, " +
                "mpRating=$mpRating, actionDiscount=$actionDiscount, isBooking=$isBooking, " +
                "isFavorite=$isFavorite, hasBooking=$hasBooking, isDelivery=$isDelivery, " +
                "hasDelivery=$hasDelivery)"
    }
}