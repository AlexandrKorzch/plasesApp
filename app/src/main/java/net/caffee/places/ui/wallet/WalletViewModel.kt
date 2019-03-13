package net.caffee.places.ui.wallet

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.GetTransactionsBody
import net.caffee.places.repo.remote.model.WalletInfo
import net.caffee.places.util.logWithTAG

class WalletViewModel(context: Application, repository: Repository)
    : BaseViewModel<WalletViewModel.Handler>(context, repository),
        LifecycleObserver {

    val items = ObservableArrayList<WalletInfo>()

    fun addBalanceClick() {}

    fun getPayments(){//todo work here
        showProgress()
        addDisposable(repository.getTransactions(GetTransactionsBody(1))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetTransactions(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }

    private fun onGetTransactions(transactions: Any) {
        logWithTAG("onGetTransactions - $transactions")
//        items.clear()
//        items.addAll(payments)
    }


    interface Handler : BaseHandler
}