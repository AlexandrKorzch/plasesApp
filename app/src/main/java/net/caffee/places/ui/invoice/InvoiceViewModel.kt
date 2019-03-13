package net.caffee.places.ui.invoice

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.CurrentBookingsAndOrders
import net.caffee.places.util.logWithTAG
import net.caffee.places.view.baseview.TwiceTestObject

class InvoiceViewModel(context: Application, repository: Repository)
: BaseViewModel<InvoiceViewModel.Handler>(context, repository),
        LifecycleObserver {

    fun fullList() = mutableListOf(
            TwiceTestObject(2, "Доставка Ресторан Икра"),
            TwiceTestObject(1, "Бронь в Караоке Бар")
    )

    fun bookingList() = mutableListOf(
            TwiceTestObject(1, "Бронь в Sky Bar"),
            TwiceTestObject(1, "Ресторан Икра")
    )

    val items = ObservableArrayList<TwiceTestObject>()


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        getCurrent()
    }

    private fun getCurrent(){
        addDisposable(repository.getCurrent()
                .doOnRequest {showProgress()}
                .subscribe(Consumer { onGetCurrent(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetCurrent(it: CurrentBookingsAndOrders) {
        logWithTAG("$it") //todo work here
    }

    fun getFullList() {
        items.clear()
        items.addAll(fullList())
    }

    fun getBookingList() {
        items.clear()
        items.addAll(bookingList())
    }

    fun getOrder(orderId: Long) {//request
            getHandler().onGetRequestForOrder()
    }

    interface Handler: BaseHandler {
        fun onGetRequestForOrder()
    }
}