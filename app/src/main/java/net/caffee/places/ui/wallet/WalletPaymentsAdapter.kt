package net.caffee.places.ui.wallet

import net.caffee.places.R
import net.caffee.places.databinding.ItemWalletPaymentBinding
import net.caffee.places.view.baseview.BaseAdapter

class WalletPaymentsAdapter(
        list: MutableList<String>?,
        private val listener: Listener
) : BaseAdapter<ItemWalletPaymentBinding, String>(list) {

    override fun bindItem(binding: ItemWalletPaymentBinding, item: String) {
        with(binding) {
            tvMoney.text = item
        }
    }

    override fun getLayoutId() = R.layout.item_wallet_payment

    interface Listener {
        fun selectItem(item: String)
    }
}