package net.caffee.places.ui.cart.adapter

import net.caffee.places.R
import net.caffee.places.databinding.ItemCartBinding
import net.caffee.places.repo.db.model.basket.Goods
import net.caffee.places.view.baseview.BaseAdapter

class CartAdapter(
        list: MutableList<Goods>,
        private val listener: Listener
) : BaseAdapter<ItemCartBinding, Goods>(list) {

    override fun getLayoutId() = R.layout.item_cart

    override fun bindItem(binding: ItemCartBinding, item: Goods) {
        binding.viewModel = CartItemViewModel(item, listener)
    }

    interface Listener {
        fun selectItem(item: String)
        fun changeCount(item: Goods)
    }
}

