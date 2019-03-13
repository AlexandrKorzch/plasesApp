package net.caffee.places.util


fun getTomorrowTimeStamp(): String? {
    return (System.currentTimeMillis()/1000 + 86400/* +1 day in seconds*/).toString()
}

fun getToDayTimeStamp(): String? {
    return (System.currentTimeMillis()/1000).toString()
}
