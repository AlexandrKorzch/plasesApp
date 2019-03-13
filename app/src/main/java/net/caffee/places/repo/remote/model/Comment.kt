package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Comment : BaseCategory() {

    @SerializedName("user_id") var userId: Long = -1
    @SerializedName("place_id") var placeId: Long = -1
    @SerializedName("parent_id") var parent_id: Long = -1
    @SerializedName("viewed") var viewed: Int = 0
    @SerializedName("name") var name: String = ""
    @SerializedName("comment") var comment: String = ""
    @SerializedName("managers_comment") var managersComment: String = ""
    @SerializedName("user") var user: User = User()
    @SerializedName("childs") var childs: List<AdminComment> = ArrayList()

    override fun toString(): String {
        return "Comment(userId=$userId, placeId=$placeId, parent_id=$parent_id, viewed=$viewed, " +
                "name='$name', comment='$comment', managersComment='$managersComment')"
    }
}