package net.caffee.places.ui.common.adapters

import android.databinding.ObservableField
import net.caffee.places.repo.remote.model.BaseCategory

class CheckableItemWithSubtitleViewModel(
        private val item: BaseCategory,
        private val subItem: String,
        private val listener: CheckableAdapter.Listener,
        private val categoryName: String
) {

    private val isChecked = ObservableField<Boolean>()

    init {
        isChecked.set(false)
        isCategorySelected()
    }

    fun checkItem() {
        isChecked.set(true)
        listener.selectItem(item)
    }

    fun item() = item

    fun subItem() = subItem

    fun isChecked() = isChecked

    private fun isCategorySelected() {
        if (item.title == categoryName) {
            isChecked.set(true)
        }
    }
}