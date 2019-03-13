package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName

class GetHallsBody(@SerializedName("place") var placeId: Long? = null)
