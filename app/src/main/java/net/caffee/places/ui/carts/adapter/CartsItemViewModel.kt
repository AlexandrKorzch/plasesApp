package net.caffee.places.ui.carts.adapter

import net.caffee.places.repo.db.model.basket.Basket

class CartsItemViewModel(
        private val item: Basket,
        private val listener: CartsAdapter.Listener) {

    fun item() = item

    fun onOpenItem() {
        listener.selectItem(item)
    }
}