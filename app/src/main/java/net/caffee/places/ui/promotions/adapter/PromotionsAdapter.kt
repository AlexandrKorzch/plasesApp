package net.caffee.places.ui.promotions.adapter

import net.caffee.places.R
import net.caffee.places.databinding.ItemPromotionsBinding
import net.caffee.places.repo.remote.model.ActionPojo
import net.caffee.places.view.baseview.BaseAdapter

class PromotionsAdapter(
        list: MutableList<ActionPojo>,
        val listener: Listener
) : BaseAdapter<ItemPromotionsBinding, ActionPojo>(list) {

    override fun getLayoutId() = R.layout.item_promotions

    override fun bindItem(binding: ItemPromotionsBinding, item: ActionPojo) {
            val viewModel = PromotionsItemViewModel(item, listener)
            binding.viewModel = viewModel
            binding.executePendingBindings()
    }

    interface Listener {
        fun selectItem(item: ActionPojo)
    }
}


