package net.caffee.places.ui.notifications.adapter

import net.caffee.places.R

// TODO change view model logic
class NotificationsItemViewModel(
        private val item: String,
        private val position: Int,
        private val listener: NotificationsAdapter.Listener
) {

    companion object {
        private const val FIRST_ITEM_IN_LIST = 0
    }

    fun selectItem() {
        if (position == FIRST_ITEM_IN_LIST) {
            listener.openNearYou(item)
        }else{
            listener.selectItem(item)
        }
    }

    fun getImage() = when (item) {
        "0" -> R.drawable.ic_nearest_places
        "1" -> R.drawable.ic_booking_for_notification_screen
        "2" -> R.drawable.ic_pin_for_notification
        "3" -> R.drawable.ic_canceled_for_notification
        "4" -> R.drawable.ic_check_for_notification
        else -> R.drawable.ic_pin_for_notification
    }

    fun isNewVisible() = when (item) {
        "0" -> true
        "1" -> true
        else -> false
    }

    fun isArrowVisible() = when (item) {
        "0" -> true
        else -> false
    }
}