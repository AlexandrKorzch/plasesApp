package net.caffee.places.ui.place.adapters

import net.caffee.places.repo.remote.model.Product

class PlaceMenuItemViewModel(val item: Product,
                             private val listener: PlaceMenuAdapter.Listener) {

    fun onOpenItem() {
        listener.selectItem(item)
    }
}