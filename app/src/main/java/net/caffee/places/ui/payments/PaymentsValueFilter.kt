package net.caffee.places.ui.payments

import android.databinding.ObservableArrayList
import android.widget.Filter

class PaymentsValueFilter (private var mapPayments: List<String>,
                           private val items: ObservableArrayList<String>) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            val filterList = ArrayList<String>()
            for (i in 0 until mapPayments.size) {
                if (mapPayments[i].toUpperCase().contains(constraint.toString().toUpperCase())) {
                    filterList.add(mapPayments[i])
                }
            }
            results.count = filterList.size
            results.values = filterList
        } else {
            results.count = mapPayments.size
            results.values = mapPayments
        }
        return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        val data = results.values as List<String>
        items.clear()
        items.addAll(data)
    }
}