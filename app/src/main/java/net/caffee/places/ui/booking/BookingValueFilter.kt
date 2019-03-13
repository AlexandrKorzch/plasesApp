package net.caffee.places.ui.booking

import android.databinding.ObservableArrayList
import android.widget.Filter
import net.caffee.places.repo.remote.model.Booking

class BookingValueFilter (private var lostBooking: List<Booking>,
                          private val items: ObservableArrayList<Booking>) : Filter() {


    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            val filterList = ArrayList<Booking>()
            for (i in 0 until lostBooking.size) {
                if (lostBooking[i].place?.title?.toUpperCase()?.contains(constraint.toString().toUpperCase())!!) {
                    filterList.add(lostBooking[i])
                }
            }
            results.count = filterList.size
            results.values = filterList
        } else {
            results.count = lostBooking.size
            results.values = lostBooking
        }
        return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        val data = results.values as List<Booking>
        items.clear()
        items.addAll(data)
    }
}