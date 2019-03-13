package net.caffee.places.ui.promotions.adapter

import net.caffee.places.repo.remote.model.ActionPojo

class PromotionsItemViewModel(
        val item: ActionPojo,
        private val listener: PromotionsAdapter.Listener) {

    fun openPromotion() {
        listener.selectItem(item)
    }
}