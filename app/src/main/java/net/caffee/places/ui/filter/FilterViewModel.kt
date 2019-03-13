package net.caffee.places.ui.filter

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.realm.RealmList
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.filter.Filter
import net.caffee.places.repo.db.model.filter.KitchenIdNamePair
import net.caffee.places.repo.db.model.filter.TypeIdNamePair
import net.caffee.places.util.CITY
import net.caffee.places.util.KITCHEN
import net.caffee.places.util.SORTSTYLE
import net.caffee.places.util.TYPE

class FilterViewModel(context: Application, repository: Repository)
    : BaseViewModel<FilterViewModel.Handler>(context, repository),
        LifecycleObserver {

    private var keyWordStr: String? = null
    private var cityStr: String? = null
    private var restaurantStr: String? = null
    private var kitchenStr: String? = null
    private var sortStyleSt: String? = null
    private var distanceStr: String? = null

    private var booking = false
    private var delivery = false

    val keyWord = ObservableField<String>()
    val city = ObservableField<String>()
    val restaurant = ObservableField<String>()
    val kitchen = ObservableField<String>()
    val sortType = ObservableField<String>()

    val distance = ObservableField<String>()
    var distanceInt: Int = 0

    val bookingObs = ObservableBoolean(false)
    val deliveryObs = ObservableBoolean(false)

    private lateinit var listStr: List<Pair<String?, ObservableField<String>>>
    private lateinit var listBool: List<Pair<Boolean, ObservableBoolean>>

    private var filter: Filter? = null

    private val changedCallBack = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            var changed = false
            listStr.forEach {if (it.first != it.second.get()) changed = true}
            listBool.forEach {if (it.first != it.second.get()) changed = true}
            getHandler().changed(changed)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        addDisposable(repository.getFilter().subscribe {
            initFieldsForCheck(it)
        })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        addDisposable(repository.getFilter().subscribe {
            initLists()
            addChangedCallBack()
            initObsFields(it)
            filter = it.copy()
        })
    }

    private fun initFieldsForCheck(filter: Filter) {
        keyWordStr = filter.search
        cityStr = filter.cityName
        sortStyleSt = filter.sortName
        restaurantStr = buildStringTypes(filter.type)
        kitchenStr = buildStringKitchens(filter.kitchenType)
        distanceStr = filter.distance.toString()
        booking = filter.hasBooking == 1
        delivery = filter.hasDelivery == 1
    }

    private fun initObsFields(filter: Filter) {
        keyWord.set(filter.search)
        city.set(filter.cityName)
        sortType.set(filter.sortName)
        restaurant.set(buildStringTypes(filter.type))
        kitchen.set(buildStringKitchens(filter.kitchenType))
        distanceInt = filter.distance ?: -1
        getHandler().setDistance(distanceInt)
        distance.set(filter.distance.toString())
        bookingObs.set(filter.hasBooking == 1)
        deliveryObs.set(filter.hasDelivery == 1)
    }

    fun chooseCityClick() = getHandler().openChooser(CITY)

    fun chooseRestaurantClick() = getHandler().openChooser(TYPE)

    fun chooseKitchenClick() = getHandler().openChooser(KITCHEN)

    fun chooseSortStyleClick() = getHandler().openChooser(SORTSTYLE)

    private fun addChangedCallBack() {
        keyWord.addOnPropertyChangedCallback(changedCallBack)
        city.addOnPropertyChangedCallback(changedCallBack)
        restaurant.addOnPropertyChangedCallback(changedCallBack)
        kitchen.addOnPropertyChangedCallback(changedCallBack)
        sortType.addOnPropertyChangedCallback(changedCallBack)
        distance.addOnPropertyChangedCallback(changedCallBack)
        bookingObs.addOnPropertyChangedCallback(changedCallBack)
        deliveryObs.addOnPropertyChangedCallback(changedCallBack)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        removeChangeCallBacks()
        dispose()
        saveState()
    }

    private fun saveState() {
        filter?.search = keyWord.get()
        filter?.hasDelivery = if (deliveryObs.get()) 1 else 0
        filter?.hasBooking = if (bookingObs.get()) 1 else 0
        filter?.distance = distanceInt
        repository.updateFilter(filter!!)
    }

    private fun dispose() {
        disposables.dispose()
        disposables.clear()
    }

    private fun removeChangeCallBacks() {
        keyWord.removeOnPropertyChangedCallback(changedCallBack)
        city.removeOnPropertyChangedCallback(changedCallBack)
        restaurant.removeOnPropertyChangedCallback(changedCallBack)
        kitchen.removeOnPropertyChangedCallback(changedCallBack)
        sortType.removeOnPropertyChangedCallback(changedCallBack)
        distance.removeOnPropertyChangedCallback(changedCallBack)
        bookingObs.removeOnPropertyChangedCallback(changedCallBack)
        deliveryObs.removeOnPropertyChangedCallback(changedCallBack)
    }

    private fun buildStringTypes(list: RealmList<TypeIdNamePair>): String {
        val typeBuilder = StringBuilder()
        list.forEachIndexed { index, typeIdNamePair ->
            typeBuilder.append(typeIdNamePair.name)
            if (index != list.size - 1) typeBuilder.append(", ")
        }
        return typeBuilder.toString()
    }

    private fun buildStringKitchens(list: RealmList<KitchenIdNamePair>): String {
        val typeBuilder = StringBuilder()
        list.forEachIndexed { index, kitchenIdNamePair ->
            typeBuilder.append(kitchenIdNamePair.name)
            if (index != list.size - 1) typeBuilder.append(", ")
        }
        return typeBuilder.toString()
    }

    private fun initLists() {

        listBool = listOf(
                Pair(delivery, deliveryObs),
                Pair(booking, bookingObs))

        listStr = listOf(
                Pair(keyWordStr, keyWord),
                Pair(cityStr, city),
                Pair(restaurantStr, restaurant),
                Pair(kitchenStr, kitchen),
                Pair(sortStyleSt, sortType),
                Pair(distanceStr, distance))
    }

    interface Handler : BaseHandler {
        fun openChooser(type: String)
        fun changed(changed: Boolean)
        fun setDistance(distanceInt: Int)
    }
}

