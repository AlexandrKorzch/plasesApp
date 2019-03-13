package net.caffee.places.ui.advantages

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.AdvantageRealm

class AdvantagesViewModel(context: Application, repository: Repository)
    : BaseViewModel<AdvantagesViewModel.Handler>(context, repository), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        addDisposable(
                repository.getAdvantages()
                        .subscribe({getHandler().onGetData(it)})
        )
    }
    interface Handler : BaseHandler {
        fun onGetData(list: List<AdvantageRealm>)
        fun skip()
    }

    fun skip() {
        getHandler().skip()
    }
}