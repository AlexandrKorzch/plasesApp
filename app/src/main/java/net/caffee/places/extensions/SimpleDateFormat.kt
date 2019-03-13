package net.caffee.places.extensions

import java.text.SimpleDateFormat
import java.util.*

private const val TIME_DAY_MONTH = "dd MMMM, HH:mm"
private const val DAY_MONTH = "dd MMMM"

fun getTimeDayMonthSimpleDateFormat() = SimpleDateFormat(TIME_DAY_MONTH, Locale.getDefault())
fun getMiniFilterSimpleDateFormat() = SimpleDateFormat(DAY_MONTH, Locale.getDefault())
