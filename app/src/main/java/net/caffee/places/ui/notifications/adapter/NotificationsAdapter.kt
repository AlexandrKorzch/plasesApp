package net.caffee.places.ui.notifications.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.databinding.ItemNotificationsBinding

class NotificationsAdapter(
        private val list: MutableList<String>,
        private val listener: Listener
) : RecyclerView.Adapter<NotificationsAdapter.NotificationsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemNotificationsHolderBinding = DataBindingUtil.inflate<ItemNotificationsBinding>(
                inflater, R.layout.item_notifications, parent, false)
        return NotificationsHolder(itemNotificationsHolderBinding)
    }

    override fun onBindViewHolder(holder: NotificationsHolder, position: Int) {
        holder.bindItem(list[position], position)
    }

    override fun getItemCount() = list.size

    inner class NotificationsHolder(
            private val binding: ItemNotificationsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: String, position: Int) {
            val viewModel = NotificationsItemViewModel(item, position, listener)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    interface Listener {
        fun openNearYou(item: String)
        fun selectItem(item: String)
    }
}