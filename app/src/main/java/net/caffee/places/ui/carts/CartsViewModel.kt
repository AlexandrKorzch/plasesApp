package net.caffee.places.ui.carts

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.basket.Basket

class CartsViewModel(context: Application, repository: Repository)
    : BaseViewModel<CartsViewModel.Handler>(context, repository),
        LifecycleObserver {

    val items = ObservableArrayList<Basket>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        addDisposable(repository.getAllBaskets()
                .subscribe {
                    items.clear()
                    items.addAll(it)
                }
        )
    }

    interface Handler : BaseHandler {
    }
}