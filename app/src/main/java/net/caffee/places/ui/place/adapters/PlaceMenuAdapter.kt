package net.caffee.places.ui.place.adapters

import net.caffee.places.R
import net.caffee.places.databinding.ItemPlaceMenuBinding
import net.caffee.places.repo.remote.model.Product
import net.caffee.places.view.baseview.BaseAdapter

class PlaceMenuAdapter(
        private val list: MutableList<Product>,
        private val listener: Listener
) : BaseAdapter<ItemPlaceMenuBinding, Product>(list) {

    override fun getLayoutId() = R.layout.item_place_menu

    override fun bindItem(binding: ItemPlaceMenuBinding, item: Product) {
            binding.viewModel = PlaceMenuItemViewModel(item, listener)
            binding.executePendingBindings()
    }

    interface Listener {
        fun selectItem(item: Product)
    }
}


