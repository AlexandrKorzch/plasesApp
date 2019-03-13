package net.caffee.places.ui.carts.adapter

import net.caffee.places.R
import net.caffee.places.databinding.ItemCartsBinding
import net.caffee.places.repo.db.model.basket.Basket
import net.caffee.places.view.baseview.BaseAdapter

class CartsAdapter(
        list: MutableList<Basket>,
        private val listener: Listener
) : BaseAdapter<ItemCartsBinding, Basket>(list) {

    override fun getLayoutId() = R.layout.item_carts

    override fun bindItem(binding: ItemCartsBinding, item: Basket) {
        binding.viewModel = CartsItemViewModel(item, listener)
    }

    interface Listener {
        fun selectItem(item: Basket)
    }
}
