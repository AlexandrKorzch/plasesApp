package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Fag : BaseCategory(){
    @SerializedName("answer") var answer: String = ""
    @SerializedName("question") var question: String = ""
}