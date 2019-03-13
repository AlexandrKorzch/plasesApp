package net.caffee.places.ui.invoice

import android.databinding.ObservableField
import net.caffee.places.R

class InvoiceItemViewModel(val title: String, opened: Boolean) {

    val isOpened = ObservableField<Boolean>(opened)
    val titleObs = ObservableField<String>(title)
    val imageId = ObservableField<Int>(R.drawable.ic_coffee_house)

    fun changeState() {
        isOpened.set(!isOpened.get()!!)
    }

}