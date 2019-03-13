package net.caffee.places.ui.common

import android.databinding.ObservableField

class PopupListOrMapViewModel(
        isShowList: Boolean,
        private val listener: Listener
) {
    private val isShowList = ObservableField<Boolean>()

    init {
        this.isShowList.set(isShowList)
    }

    fun isShowList() = this.isShowList

    fun showList() {
        this.isShowList.set(true)
        listener.openListView()
    }

    fun showMap() {
        this.isShowList.set(false)
        listener.openMapView()
    }

    interface Listener {
        fun openListView()
        fun openMapView()
    }
}