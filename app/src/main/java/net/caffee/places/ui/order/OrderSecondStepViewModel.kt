package net.caffee.places.ui.order

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.R
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.basket.Basket
import net.caffee.places.repo.db.model.basket.Goods
import net.caffee.places.repo.db.model.order.OrderDb
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.AddOrderBody
import net.caffee.places.repo.remote.model.Cart
import net.caffee.places.util.SingleLiveEvent
import net.caffee.places.util.calculateTotalPrice


class OrderSecondStepViewModel(context: Application, repository: Repository)
    : BaseViewModel<OrderSecondStepViewModel.Handler>(context, repository),
        LifecycleObserver {


    val cartCount = SingleLiveEvent<Int>()

    val paymentIsChecked = ObservableBoolean(false)

    val walletChecked = ObservableBoolean(false)
    val cardChecked = ObservableBoolean(false)
    val cashChecked = ObservableBoolean(false)

    val totalProductsPriceObs = ObservableFloat(0F)
    val deliveryPriceObs = ObservableFloat(0F)
    val totalPriceObs = ObservableFloat(0F)

    val commentObs = ObservableField<String>()

    private var placeId: Long = 0L
    private lateinit var order: OrderDb

    private val checkerGroup = mapOf(R.id.iv_wallet to walletChecked,
            R.id.iv_card to cardChecked, R.id.iv_cash to cashChecked)

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        getBasketRequest(placeId)
        getGoodsCount(placeId)
        getBasket(placeId)
        getOrder(placeId)
    }

    private fun getBasketRequest(placeId: Long) {
        addDisposable(repository.getCarts(placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetCartsFromServer(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun getGoodsCount(placeId: Long) {
        addDisposable(repository.getGoodsCountInBasket(placeId)
                .subscribe { cartCount.value = it })
    }

    private fun getBasket(placeId: Long) {
        addDisposable(repository.getBasketByIdOrFirst(placeId)
                .subscribe { onGetBasket(it) })
    }

    private fun getOrder(placeId: Long) {
        addDisposable(repository.getOrderById(placeId)
                .subscribe { onGetOrder(it) })
    }

    private fun onGetCartsFromServer(cart: Cart) {
        deliveryPriceObs.set(cart.deliverySum)
        totalPriceObs.set(totalProductsPriceObs.get() + cart.deliverySum)
    }

    private fun onGetOrder(order: OrderDb) {
        this.order = order
    }

    private fun onGetBasket(basket: Basket) {
        calculateBasketTotalPrice(basket.goods)
    }

    private fun calculateBasketTotalPrice(goods: List<Goods>) {
        totalProductsPriceObs.set(calculateTotalPrice(goods))
        totalPriceObs.set(totalProductsPriceObs.get() + deliveryPriceObs.get())
    }

    fun paymentClick(v: View) {
        paymentIsChecked.set(true)
        checkerGroup[v.id]?.set(true)
        checkerGroup.forEach { if (it.key != v.id) it.value.set(false) }
    }

    fun nextClick() {
        addDisposable(repository.getOrderById(placeId)
                .subscribe { makeOrder(it.copy()) })
    }

    private fun makeOrder(orderDb: OrderDb) {
        modifyOrderAndSetToDb(orderDb)
        val order = prepareOrderBody(orderDb)
        orderRequest(order)
    }

    private fun orderRequest(order: AddOrderBody) {
        addDisposable(repository.addOrder(order)
                .subscribe(Consumer { getHandler().openOrderThirdStep() },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun prepareOrderBody(orderDb: OrderDb): AddOrderBody {
        return AddOrderBody(placeId,
                getPaymentType(),
                orderDb.cityId!!,
                orderDb.name!!,
                orderDb.phone!!,
                orderDb.address!!)
    }

    private fun modifyOrderAndSetToDb(orderDb: OrderDb) {
        orderDb.apply {
            comment = commentObs.get()
            payType = getPaymentTypeName()
        }
        repository.setNewOrder(orderDb)
    }

    private fun getPaymentType(): Int {
        return when (true) {
            walletChecked.get() -> 3
            cardChecked.get() -> 2
            cashChecked.get() -> 4
            else -> 0
        }
    }

    private fun getPaymentTypeName(): String {
        return when (true) {
            walletChecked.get() -> getApplication<Application>().getString(R.string.order_wallet)
            cardChecked.get() -> getApplication<Application>().getString(R.string.order_card)
            cashChecked.get() -> getApplication<Application>().getString(R.string.order_cash)
            else -> ""
        }
    }

    fun setPlaceId(placeId: Long) {
        this.placeId = placeId
    }

    interface Handler : BaseHandler {
        fun openOrderThirdStep()
    }
}