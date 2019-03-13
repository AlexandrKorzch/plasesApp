package net.caffee.places.repo.remote.model

import com.google.gson.annotations.SerializedName
import java.util.*

class CurrentBookingsAndOrders {

    @SerializedName("bookings") var bookings: List<CurrentBooking> = ArrayList()
    @SerializedName("orders") var orders: List<Order> = ArrayList()

    override fun toString(): String {
        return "CurrentBookingsAndOrders(bookings=$bookings, orders=$orders)"
    }
}