package net.caffee.places.ui.payments

import net.caffee.places.R
import net.caffee.places.databinding.ItemPaymentBinding
import net.caffee.places.view.baseview.BaseAdapter

class PaymentsAdapter (
        list: MutableList<String>?,
        private val listener: Listener
) : BaseAdapter<ItemPaymentBinding, String>(list) {

    override fun bindItem(binding: ItemPaymentBinding, item: String) {
        with(binding){
            tvSum.text = item
        }
    }

    override fun getLayoutId() = R.layout.item_payment

    interface Listener {
        fun selectItem(item: String)
    }
}