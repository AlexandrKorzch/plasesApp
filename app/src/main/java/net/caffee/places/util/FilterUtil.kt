package net.caffee.places.util

import net.caffee.places.global.TODAY
import net.caffee.places.global.TOMORROW
import net.caffee.places.repo.db.model.filter.Filter
import net.caffee.places.repo.remote.model.GetFilteredPlacesBody
import net.caffee.places.ui.places.PlacesFragment


fun initFromDbFilter(filterBody: GetFilteredPlacesBody, filterDb: Filter): GetFilteredPlacesBody {
    filterBody.distance = if(filterDb.distance == 0) null else filterDb.distance
    filterBody.search = if(filterDb.search == "") null else filterDb.search
    filterBody.cityId = filterDb.cityId
    filterBody.isBooking = filterDb.isBooking
    filterBody.hasBooking = filterDb.hasBooking
    filterBody.hasDelivery = filterDb.hasDelivery
    filterBody.order = filterDb.sortId?.toInt()
    filterDb.type.forEach { filterBody.type?.add(it.id) }
    filterDb.kitchenType.forEach { filterBody.kitchenType?.add(it.id) }

    when(filterDb.discountDate){
        TODAY -> {filterBody.discountDate = getToDayTimeStamp()}
        TOMORROW -> {filterBody.discountDate = getTomorrowTimeStamp()}
        else -> {filterBody.discountDate  = filterDb.discountDate?.toLong()?.div(1000)?.toString()}
    }

    return filterBody
}

fun initFromType(typeFrFl: Int, filterBody: GetFilteredPlacesBody): GetFilteredPlacesBody {
    when (typeFrFl) {
        PlacesFragment.FAVORITE_PLACES -> filterBody.isFavorite = 1
        PlacesFragment.PLACES_WITH_DELIVERY -> filterBody.hasDelivery = 1
    }
    return filterBody
}