package net.caffee.places.ui.promotions

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
import net.caffee.places.repo.remote.model.ActionPojo
import net.caffee.places.repo.remote.model.GetActionsBody

class PromotionsViewModel(context: Application, repository: Repository)
    : BaseViewModel<PromotionsViewModel.Handler>(context, repository),
        LifecycleObserver {

    val items = ObservableArrayList<ActionPojo>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        showProgress()
        addDisposable(repository.getActions(GetActionsBody())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetActions(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetActions(actions: List<ActionPojo>) {
        items.clear()
        items.addAll(actions)
    }

    interface Handler : BaseHandler {
    }
}