package net.caffee.places.repo.db.model.filter

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Filter : RealmObject() {

    @PrimaryKey
    var id: Long = 1
    var sortId: Long? = null
    var sortName: String? = null
    var search: String? = null
    var cityId: Long? = null
    var cityName: String? = null
    var distance: Int? = null
    var discountDate: String? = null
    var isBooking: Int? = null
    var hasBooking: Int? = null
    var hasDelivery: Int? = null
    var type: RealmList<TypeIdNamePair> = RealmList()
    var kitchenType: RealmList<KitchenIdNamePair> = RealmList()


    fun copy(): Filter {

        val filter = Filter()

        filter.id = id
        filter.sortId = sortId
        filter.sortName = sortName
        filter.search = search
        filter.cityId = cityId
        filter.cityName = cityName
        filter.distance = distance
        filter.discountDate = discountDate
        filter.isBooking = isBooking
        filter.hasBooking = hasBooking
        filter.hasDelivery = hasDelivery

        copyListType(this.type, filter.type)
        copyListKitchen(this.kitchenType, filter.kitchenType)

        return filter
    }

    //todo need refactoring
    private fun copyListType(oldList: RealmList<TypeIdNamePair>, newList: RealmList<TypeIdNamePair>) {
        for (pair in oldList) {
            val newPair = TypeIdNamePair()
            newPair.id = pair.id
            newPair.name = pair.name
            newList.add(newPair)
        }
    }

    private fun copyListKitchen(oldList: RealmList<KitchenIdNamePair>, newList: RealmList<KitchenIdNamePair>) {
        for (pair in oldList) {
            val newPair = KitchenIdNamePair()
            newPair.id = pair.id
            newPair.name = pair.name
            newList.add(newPair)
        }
    }

    override fun toString(): String {
        return "Filter(id=$id, sortId='$sortId', sortName='$sortName', " +
                "search='$search', cityId='$cityId', cityName='$cityName', " +
                "distance='$distance', discountDate='$discountDate', " +
                "isBooking=$isBooking, hasBooking=$hasBooking, type=$type, " +
                "kitchenType=$kitchenType)"
    }
}