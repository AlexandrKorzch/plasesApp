package net.caffee.places.ui.booking

import android.databinding.ObservableBoolean
import net.caffee.places.R
import net.caffee.places.databinding.ItemBookingBinding
import net.caffee.places.repo.remote.model.Booking
import net.caffee.places.ui.booking.BookingFragment.Companion.PAST_BOOKING
import net.caffee.places.view.baseview.BaseAdapter

class BookingAdapter(
        val type: Int,
        list: MutableList<Booking>?,
        private val listener: Listener
) : BaseAdapter<ItemBookingBinding, Booking>(list) {

    override fun getLayoutId() = R.layout.item_booking

    override fun bindItem(binding: ItemBookingBinding, item: Booking) {
        binding.bookingItem = BookingItem(item, type, listener)
    }

    interface Listener {
        fun selectItem(bookingId: Long)
    }
}

class BookingItem(val booking: Booking, val type: Int, val listener: BookingAdapter.Listener) {
    val showExtra = ObservableBoolean(type == PAST_BOOKING)
}