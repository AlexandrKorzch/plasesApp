package net.caffee.places.ui.wallet.info

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.WalletInfo

class InfoViewModel(context: Application, repository: Repository)
    : BaseViewModel<InfoViewModel.Handler>(context, repository),
        LifecycleObserver {

    val items = ObservableArrayList<WalletInfo>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        showProgress()
        addDisposable(repository.getWalletInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetWalletInfo(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }

    private fun onGetWalletInfo(list: List<WalletInfo>) {
        items.clear()
        items.addAll(list)
    }


    interface Handler : BaseHandler
}