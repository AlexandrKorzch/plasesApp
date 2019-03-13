package net.caffee.places.extensions

import java.util.*

fun Calendar.isNotPastTime() = this.timeInMillis >= Calendar.getInstance().timeInMillis

fun Calendar.setCurrentTime() {
    val currentTimeInMillis = Calendar.getInstance().timeInMillis
    this.time = Date(currentTimeInMillis)
}