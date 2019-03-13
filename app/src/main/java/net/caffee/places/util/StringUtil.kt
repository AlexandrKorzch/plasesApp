package net.caffee.places.util

import android.databinding.BindingAdapter
import android.widget.TextView


@BindingAdapter("numberText")
fun setNumberText(view: TextView, number: Int) {
    view.text = number.toString()
}

@BindingAdapter("bind:balance", "bind:symbol")
fun setBalanceText(view: TextView, balance: Int, symbol: String) {

    var balanceStr = balance.toString()

    if (balanceStr.length >= 4) {

    balanceStr = StringBuilder()
            .append(balanceStr.substring(0,balanceStr.length-3))
            .append(" ")
            .append(balanceStr.substring(balanceStr.length-3,balanceStr.length))
            .toString()
    }

    view.text = StringBuilder()
            .append(balanceStr)
            .append(" ")
            .append(symbol)
            .toString()
}


