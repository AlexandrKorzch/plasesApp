package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName
import java.util.*

class ProdCategory : BaseCategory() {
    @SerializedName("categories") var subcategories: List<ProdSubCategory> = ArrayList()
}