package net.caffee.places.ui.payments

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableArrayList
import android.support.v7.widget.SearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.GetTransactionsBody
import net.caffee.places.util.logWithTAG

class PaymentsViewModel(context: Application, repository: Repository)
    : BaseViewModel<PaymentsViewModel.Handler>(context, repository),
        LifecycleObserver {

    private val mapPayments = listOf(
            "1234 ₸",
            "1559 ₸",
            "345 ₸",
            "1233 ₸",
            "4565 ₸",
            "6548 ₸",
            "9879 ₸",
            "3219 ₸",
            "7854 ₸",
            "3214 ₸",
            "9874 ₸",
            "9512 ₸",
            "325 ₸",
            "7533 ₸",
            "7896 ₸",
            "1254 ₸",
            "7899 ₸",
            "3578 ₸")

    val items = ObservableArrayList<String>()
    val filter = PaymentsValueFilter(mapPayments, items)

//    fun getPayments() = items.addAll(mapPayments)

    fun getPayments(){//todo work here
        showProgress()
        addDisposable(repository.getTransactions(GetTransactionsBody(2))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetTransactions(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }
//
    private fun onGetTransactions(transactions: Any) {
        logWithTAG("onGetTransactions - $transactions")
//        items.clear()
//        items.addAll(payments)
    }

    val searchChangeListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
            filter.filter(newText)
            return true }
    }

    interface Handler : BaseHandler
}
