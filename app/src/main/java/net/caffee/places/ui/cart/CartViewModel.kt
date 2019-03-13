package net.caffee.places.ui.cart

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.basket.Basket
import net.caffee.places.repo.db.model.basket.Goods
import net.caffee.places.util.calculateTotalPrice
import net.caffee.places.util.getUpdatedBasket
import net.caffee.places.util.updateCountInGoods

class CartViewModel(context: Application, repository: Repository)
    : BaseViewModel<CartViewModel.Handler>(context, repository),
        LifecycleObserver {

    val totalPriceObs = ObservableFloat()
    val placeName = ObservableField<String>()
    val placeImage = ObservableField<String>()
    val items = ObservableArrayList<Goods>()

    lateinit var basket: Basket
    lateinit var list: List<Goods>

    fun orderClick() = getHandler().orderClick()

    fun getData(placeId: Long) {
        addDisposable(
                repository.getBasketByIdOrFirst(placeId)
                        .subscribe { showBasket(it) })
    }

    private fun showBasket(it: Basket) {
        basket = it
        list = it.copy().goods
        items.clear()
        items.addAll(list)
        placeName.set(it.placeName)
        placeImage.set(it.placeImage)
        calculate()
    }

    fun update(item: Goods) {
        updateCountInGoods(item, list)
        calculate()
    }

    private fun calculate() {
        totalPriceObs.set(calculateTotalPrice(list))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        repository.addBasket(getUpdatedBasket(basket, list))
    }

    interface Handler : BaseHandler {
        fun orderClick()
    }
}