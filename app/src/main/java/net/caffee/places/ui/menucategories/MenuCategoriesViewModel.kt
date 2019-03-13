package net.caffee.places.ui.menucategories

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.GetProductsBody
import net.caffee.places.repo.remote.model.Product
import net.caffee.places.util.SingleLiveEvent

class MenuCategoriesViewModel(context: Application, repository: Repository)
    : BaseViewModel<MenuCategoriesViewModel.Handler>(context, repository), LifecycleObserver {

    private var placeId: Long = 0L
    private var subCategoryId: Long = 0L

    lateinit var cartCount : SingleLiveEvent<Int>
    lateinit var bookingCount : SingleLiveEvent<Int>

    val productsObs = ObservableArrayList<Product>()


    fun createEvents() {
        cartCount = SingleLiveEvent<Int>()
        bookingCount = SingleLiveEvent<Int>()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        showProgress()
        addDisposable(repository.getProducts(GetProductsBody(placeId, subCategoryId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetProducts(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }

    fun getBasketsCount() {
        bookingCount.postValue(0)
        addDisposable(repository.getGoodsCount()
                .subscribe {onGetBusketCount(it)})
    }

    private fun onGetBusketCount(count:Int){
        cartCount.postValue(count)
    }

    private fun onGetProducts(products: List<Product>) {
        productsObs.clear()
        productsObs.addAll(products)
    }

    fun setIds(subCategoryId: Long, placeId: Long) {
        this.subCategoryId = subCategoryId
        this.placeId = placeId
    }

    interface Handler : BaseHandler {

    }
}