package net.caffee.places.ui.common.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.databinding.ItemBaseCheckableBinding
import net.caffee.places.repo.remote.model.BaseCategory

class CheckableAdapter(
        private val list: MutableList<BaseCategory>,
        private val listener: Listener,
        private val categoryName: String
) : RecyclerView.Adapter<CheckableAdapter.CheckableHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckableHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemPlacesBinding = DataBindingUtil.inflate<ItemBaseCheckableBinding>(
                inflater, R.layout.item_base_checkable, parent, false)
        return CheckableHolder(itemPlacesBinding)
    }

    override fun onBindViewHolder(holder: CheckableHolder, position: Int) {
        holder.bindItem(list[position])
    }

    override fun getItemCount() = list.size


    inner class CheckableHolder(
            private val binding: ItemBaseCheckableBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: BaseCategory) {
            val viewModel = CheckableItemViewModel(item, listener, categoryName)
            listener.addedCategory(viewModel)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    interface Listener {
        fun addedCategory(viewModel: CheckableItemViewModel)
        fun selectItem(item: BaseCategory)
        fun unSelectItem(item: BaseCategory)
    }
}