package net.caffee.places.ui.order

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.realm.RealmList
import net.caffee.places.R
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.basket.Basket
import net.caffee.places.repo.db.model.basket.Goods
import net.caffee.places.repo.db.model.order.OrderDb
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.Profile
import net.caffee.places.util.CITY
import net.caffee.places.util.SingleLiveEvent
import net.caffee.places.util.calculateTotalPrice

class OrderFirstStepViewModel(context: Application, repository: Repository)
    : BaseViewModel<OrderFirstStepViewModel.Handler>(context, repository),
        LifecycleObserver {

    val cartCount = SingleLiveEvent<Int>()

    val edit = ObservableBoolean(false)

    val userCity = ObservableField<String>("")
    val userPhone = ObservableField<String>("")
    val userLastName = ObservableField<String>("")
    val userFirstName = ObservableField<String>("")

    val userStreet = ObservableField<String>("")
    val userAddress = ObservableField<String>("")

    val totalPriceObs = ObservableFloat(0F)

    private var placeId: Long = 0L
    private var cityIdFromServer: Long? = -1L
    private lateinit var list: List<Goods>
    private lateinit var basket: Basket

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        getBasketsCount()
        getProfile()
        getFilter()
        getBasket(placeId)
    }

    private fun getBasketsCount() {
        addDisposable(repository.getGoodsCountInBasket(placeId)
                .subscribe { cartCount.value = it })
    }

    private fun getProfile() {
        addDisposable(repository.getProfile()
                .subscribe(Consumer { onGetProfile(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun getFilter() {
        addDisposable(repository.getFilter().subscribe {
            userCity.set(it.cityName)
            cityIdFromServer = it.cityId
        })
    }

    private fun getBasket(placeId: Long) {
        addDisposable(repository.getBasketByIdOrFirst(placeId)
                .subscribe { onGetBasket(it) })
    }

    private fun onGetBasket(it: Basket) {
        this.basket = it.copy()
        calculateBasketTotalPrice(it.goods)
    }

    private fun onGetProfile(profile: Profile) {
        with(profile) {
            if(userFirstName.get()?.isEmpty()!!)userFirstName.set(firstName)
            if(userLastName.get()?.isEmpty()!!)userLastName.set(lastName)
            if(userPhone.get()?.isEmpty()!!)userPhone.set(phone)
        }
    }

    private fun calculateBasketTotalPrice(goods: RealmList<Goods>) {
        totalPriceObs.set(calculateTotalPrice(goods))
    }

    fun setPlaceId(placeId: Long) {
        this.placeId = placeId
    }

    fun editClick() = edit.set(true)

    fun userCityClick() = getHandler().openChooser(CITY)

    fun nextClick() {
        if (!edit.get() && isFieldsValid()) {
            repository.setNewOrder(OrderDb(placeId, basket).apply {
                name = userFirstName.get() + " " + userLastName.get()
                address = userAddress.get()
                street = userStreet.get()
                phone = userPhone.get()
                cityId = cityIdFromServer
            })
            getHandler().openSecondStep()
        } else {
            edit.set(false)
        }
    }

    private fun isFieldsValid(): Boolean {
        return if (userFirstName.get()?.isNotEmpty() == true
                && userLastName.get()?.isNotEmpty() == true
                && userAddress.get()?.isNotEmpty() == true
                && userStreet.get()?.isNotEmpty() == true
                && userPhone.get()?.isNotEmpty() == true
                && userCity.get()?.isNotEmpty() == true) {
            true
        } else {
            showError(getApplication<Application>().getString(R.string.order_wrong_data))
            false
        }
    }

    interface Handler : BaseHandler {
        fun openChooser(type: String)
        fun openSecondStep()
    }
}