package net.caffee.places.ui.places.adapter

import android.databinding.ObservableBoolean
import net.caffee.places.R
import net.caffee.places.databinding.ItemPlacesBinding
import net.caffee.places.repo.remote.model.Place
import net.caffee.places.view.baseview.BaseAdapter


class PlacesAdapter(
        list: MutableList<Place>?,
        private val listener: Listener
) : BaseAdapter<ItemPlacesBinding, Place>(list) {

    override fun getLayoutId() = R.layout.item_places

    override fun bindItem(binding: ItemPlacesBinding, item: Place) {
        binding.place = item
        binding.favoriteState = FavoriteStateListener(listener, item)
        binding.listener = listener
    }

    interface Listener {
        fun openPlace(placeId: Long)
        fun openBooking(placeId: Long)
        fun openDelivery(placeId: Long)
        fun setFavoritePlace(placeId: Long, favorite: Boolean)
    }
}

class FavoriteStateListener(val listener: PlacesAdapter.Listener, val item: Place) {
    val favoriteState = ObservableBoolean(item.isFavorite)
    fun favoriteClick(){
        favoriteState.set(!favoriteState.get())
        item.isFavorite = !item.isFavorite
        listener.setFavoritePlace(item.id, favoriteState.get())
    }
}
