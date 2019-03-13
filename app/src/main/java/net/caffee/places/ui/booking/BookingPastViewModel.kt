package net.caffee.places.ui.booking

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableArrayList
import android.support.v7.widget.SearchView
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.Booking
import net.caffee.places.repo.remote.model.GetBookingsBody

class BookingPastViewModel(context: Application, repository: Repository)
    : BaseViewModel<BookingPastViewModel.Handler>(context, repository),
        LifecycleObserver {

    var list: ArrayList<Booking> = ArrayList()

    val items = ObservableArrayList<Booking>()
    val filter = BookingValueFilter(list, items)

    fun getBookingRequest(type: Int) {
        showProgress()
        addDisposable(repository.getBookings(GetBookingsBody(type))
                .subscribe(Consumer { onGetBookings(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetBookings(list: List<Booking>) {
        addItems(list)
    }

    private fun addItems(currentList: List<Booking>) {
        list.clear()
        list.addAll(currentList)
        items.clear()
        items.addAll(currentList)
    }

    val searchChangeListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
            filter.filter(newText)
            return true
        }
    }

    interface Handler : BaseHandler
}