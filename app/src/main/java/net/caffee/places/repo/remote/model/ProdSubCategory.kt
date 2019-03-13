package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class ProdSubCategory: BaseCategory() {

    @SerializedName("place_id") var placeId: Long = 0L
    @SerializedName("main_category_id") var mainCategoryId: Long = 0L
}