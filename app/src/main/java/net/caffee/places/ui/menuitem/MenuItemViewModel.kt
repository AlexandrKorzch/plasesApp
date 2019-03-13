package net.caffee.places.ui.menuitem

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import android.databinding.ObservableInt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.AddProductBody
import net.caffee.places.repo.remote.model.GetProductBody
import net.caffee.places.repo.remote.model.PlaceExtended
import net.caffee.places.repo.remote.model.Product
import net.caffee.places.util.addToBasketOrChangeIfExist

class MenuItemViewModel(context: Application, repository: Repository)
    : BaseViewModel<MenuItemViewModel.Handler>(context, repository),
        LifecycleObserver {

    companion object {
        private const val MIN = 1
        private const val MAX = 99
    }

    private var productId: Long = 0L
    private var placeId: Long = 0L
    private var placeName: String? = null
    private var placeImage: String? = null

    val fullPrice = ObservableFloat()
    val itemAmount = ObservableInt()
    val productObs = ObservableField<Product>()

    init {
        itemAmount.set(MIN)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        showProgress()
        getProduct()
        getPlace()
    }

    private fun getPlace() {
        addDisposable(repository.getPlace(placeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetPlace(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun getProduct() {
        addDisposable(repository.getProduct(GetProductBody(productId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetProduct(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetProduct(product: Product) {
        productObs.set(product)
        itemAmount.set(MIN)
        calculateFullPrice()
    }

    private fun onGetPlace(place: PlaceExtended?) {
        placeName = place?.title
        placeImage = place?.image
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        productObs.set(null)
    }

    fun changeAmount(increment: Boolean) {
        itemAmount.get().let {
            if ((increment && it < MAX) || (!increment && it > MIN)) {
                itemAmount.set(if (increment) it.inc() else it.dec())
                calculateFullPrice()
            }
        }
    }

    private fun calculateFullPrice() {
        productObs.get()?.price?.times(itemAmount.get())?.let {
            fullPrice.set(it)
        }
    }

    fun onCloseClick() {
        getHandler().closeFragmentDialog()
    }

    fun onAddToCart() {
        addDisposable(repository.addProductToCarts(AddProductBody(productObs.get()?.id!!, itemAmount.get()))
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onAddedProduct() },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onAddedProduct() = addProductToDB()

    private fun addProductToDB() {
        addDisposable(repository.getBasketByIdOrCreate(placeId)
                .subscribe {
                    repository.addBasket(
                            addToBasketOrChangeIfExist(it, productObs.get(), itemAmount.get(),
                                    placeName, placeImage))
                })
        onCloseClick()
    }

    fun setProductId(productId: Long, placeId: Long) {
        this.productId = productId
        this.placeId = placeId
    }

    interface Handler : BaseHandler {
        fun closeFragmentDialog()
    }
}