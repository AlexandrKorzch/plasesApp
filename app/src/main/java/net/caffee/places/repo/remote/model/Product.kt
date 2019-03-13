package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class Product {

    @SerializedName("status") var status: Int? = null
    @SerializedName("created_by") var createdBy: Int? = null
    @SerializedName("updated_by") var updatedBy: Int? = null
    @SerializedName("category_id") var categoryId: Int? = null
    @SerializedName("main_category_id") var mainCategoryId: Int? = null
    @SerializedName("id") var id: Long = 0L
    @SerializedName("place_id") var placeId: Long? = null
    @SerializedName("created_at") var createdAt: Long? = null
    @SerializedName("updated_at") var updatedAt: Long? = null
    @SerializedName("price") var price: Float? = null
    @SerializedName("weight") var weight: Float? = null
    @SerializedName("image") var image: String? = null
    @SerializedName("title") var title: String? = null
    @SerializedName("dimension") var dimension: String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("composition") var composition: String? = null

    override fun toString(): String {
        return "Product(createdBy=$createdBy, placeId=$placeId, weight=$weight, status=$status, " +
                "mainCategoryId=$mainCategoryId, categoryId=$categoryId, image=$image, id=$id, " +
                "dimension=$dimension, title=$title, updatedAt=$updatedAt, price=$price, " +
                "description=$description, composition=$composition, createdAt=$createdAt, " +
                "updatedBy=$updatedBy)"
    }
}