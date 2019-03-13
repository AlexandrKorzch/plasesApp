package net.caffee.places.ui.place.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.databinding.ItemPlaceGalleryBinding

class PlaceGalleryAdapter(
        private val list: MutableList<String>,
        private val listener: Listener
) : RecyclerView.Adapter<PlaceGalleryAdapter.PlaceGalleryViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): PlaceGalleryAdapter.PlaceGalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemPlacesBinding = DataBindingUtil.inflate<ItemPlaceGalleryBinding>(
                inflater, R.layout.item_place_gallery, parent, false)
        return PlaceGalleryViewHolder(itemPlacesBinding)
    }

    override fun onBindViewHolder(holder: PlaceGalleryViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    override fun getItemCount() = list.size

    interface Listener

    inner class PlaceGalleryViewHolder(
            private val binding: ItemPlaceGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: String) {
            binding.item = item
            binding.listener = listener
            binding.executePendingBindings()
        }
    }
}