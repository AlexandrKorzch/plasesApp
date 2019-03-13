package net.caffee.places.ui.places.filter

import android.widget.Filter
import net.caffee.places.repo.remote.model.Place

class PlacesFilter (private var list: List<Place>,
                    val function: (List<Place>) -> Unit) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            val filterList = ArrayList<Place>()
            for (i in 0 until list.size) {
                if (list[i].title.toUpperCase().contains(constraint.toString().toUpperCase())) {
                    filterList.add(list[i])
                }
            }
            results.count = filterList.size
            results.values = filterList
        } else {
            results.count = list.size
            results.values = list
        }
        return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        val data = results.values as List<Place>
        function(data)
    }
}