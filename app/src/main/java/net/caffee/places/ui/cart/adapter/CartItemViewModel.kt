package net.caffee.places.ui.cart.adapter

import android.databinding.ObservableField
import net.caffee.places.repo.db.model.basket.Goods

class CartItemViewModel(
        var item: Goods,
        private val listener: CartAdapter.Listener) {

    val goods = ObservableField<Goods>(item)

    companion object {
        private const val MIN_ITEM_AMOUNT = 0
        private const val MAX_ITEM_AMOUNT = 99
    }

    private val itemAmount = ObservableField<String>()

    init {
        itemAmount.set(item.count.toString())
    }

    fun incItemAmount() {
        itemAmount.get()?.let {
            if (it.toInt() < MAX_ITEM_AMOUNT) {
                itemAmount.set(it.toInt().inc().toString())
                item = item.copy().apply { count = it.toInt().inc() }
                goods.set(item)
                listener.changeCount(item)
            }
        }
    }

    fun decItemAmount() {
        itemAmount.get()?.let {
            if (it.toInt() > MIN_ITEM_AMOUNT) {
                itemAmount.set(it.toInt().dec().toString())
                item = item.copy().apply { count = it.toInt().dec() }
                goods.set(item)
                listener.changeCount(item)
            }
        }
    }

    fun stringItemAmount() = itemAmount
}