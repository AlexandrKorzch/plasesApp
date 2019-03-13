package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class User : BaseCategory(){

    @SerializedName("city_id") var cityId: Long = -1
    @SerializedName("place_id") var placeId: Long = -1
    @SerializedName("blocked_by") var blockedBy: Long = -1
    @SerializedName("blocked_at") var blockedAt: Long = -1
    @SerializedName("approved_at") var approvedAt: Long = -1
    @SerializedName("approved_by") var approvedBy: Long = -1

    @SerializedName("type") var type: Int = -1
    @SerializedName("notifications") var notifications: Int = -1
    @SerializedName("wallet_balance") var walletBalance: Int = -1

    @SerializedName("email_verified") var emailVerified: Int = 0

    @SerializedName("lang") var lang: String = ""
    @SerializedName("phone") var phone: String = ""
    @SerializedName("email") var email: String = ""
    @SerializedName("auth_key") var authKey: String = ""
    @SerializedName("last_name") var lastName: String = ""
    @SerializedName("first_name") var firstName: String = ""
    @SerializedName("password_hash") var passwordHash: String = ""
    @SerializedName("blocked_reason") var blockedReason: String = ""
    @SerializedName("email_confirm_token") var emailConfirmToken: String = ""
    @SerializedName("password_reset_token") var passwordResetToken: String = ""

    override fun toString(): String {
        return "User(cityId=$cityId, placeId=$placeId, blockedBy=$blockedBy, blockedAt=$blockedAt, " +
                "approvedAt=$approvedAt, approvedBy=$approvedBy, type=$type, " +
                "notifications=$notifications, walletBalance=$walletBalance, " +
                "emailVerified=$emailVerified, lang='$lang', phone='$phone', " +
                "email='$email', authKey='$authKey', lastName='$lastName', " +
                "firstName='$firstName', passwordHash='$passwordHash', " +
                "blockedReason='$blockedReason', emailConfirmToken='$emailConfirmToken', " +
                "passwordResetToken='$passwordResetToken')"
    }
}