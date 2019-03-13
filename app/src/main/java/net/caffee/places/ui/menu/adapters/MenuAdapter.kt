package net.caffee.places.ui.menu.adapters

import net.caffee.places.R
import net.caffee.places.databinding.ItemMenuBinding
import net.caffee.places.repo.remote.model.ProdSubCategory
import net.caffee.places.view.baseview.BaseAdapter

class MenuAdapter(
        private val list: MutableList<ProdSubCategory>,
        private val listener: Listener
) :  BaseAdapter<ItemMenuBinding, ProdSubCategory>(list) {

    override fun getLayoutId() = R.layout.item_menu

    override fun bindItem(binding: ItemMenuBinding, item: ProdSubCategory) {
        binding.viewModel = MenuItemViewModel(item, listener)
        binding.executePendingBindings()
    }

    interface Listener {
        fun selectItem(item: ProdSubCategory)
    }
}


