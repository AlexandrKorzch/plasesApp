package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class AdminComment : BaseCategory() {

    @SerializedName("name") var name: String = ""
    @SerializedName("comment") var comment: String = ""

    override fun toString(): String {
        return "AdminComment(name='$name', comment='$comment')"
    }
}