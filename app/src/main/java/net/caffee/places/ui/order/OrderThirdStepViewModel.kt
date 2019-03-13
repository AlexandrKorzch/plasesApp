package net.caffee.places.ui.order

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.order.OrderDb
import net.caffee.places.util.logWithTAG

class OrderThirdStepViewModel(context: Application, repository: Repository)
    : BaseViewModel<OrderThirdStepViewModel.Handler>(context, repository),
        LifecycleObserver {


    private var placeId: Long = 0L


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        addDisposable(repository.getOrderById(placeId)
                .subscribe { onGetOrder(it) })
    }

    private fun onGetOrder(order: OrderDb) {//todo work here
//        this.order = order
        logWithTAG("$order")
    }

    fun setPlaceId(placeId: Long) {
        this.placeId = placeId
    }

    interface Handler : BaseHandler {}
}