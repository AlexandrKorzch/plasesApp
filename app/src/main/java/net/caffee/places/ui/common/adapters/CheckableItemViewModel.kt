package net.caffee.places.ui.common.adapters

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import net.caffee.places.repo.remote.model.BaseCategory

class CheckableItemViewModel(
        private val item: BaseCategory,
        private val listener: CheckableAdapter.Listener,
        private val categoryName: String) {

    val isChecked = ObservableBoolean(false)
    val image = ObservableField<String>("")

    init {
        image.set(item.image)
    }

    fun checkItem() {
        isChecked.set(!isChecked.get())
        if(isChecked.get()){
            listener.selectItem(item)
        }else{
            listener.unSelectItem(item)
        }
    }

    fun item() = item

}