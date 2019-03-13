package net.caffee.places.ui.wallet.info

import net.caffee.places.R
import net.caffee.places.databinding.ItemInfoBinding
import net.caffee.places.repo.remote.model.WalletInfo
import net.caffee.places.view.baseview.BaseAdapter

class InfoAdapter(
        list: MutableList<WalletInfo>?,
        private val listener: Listener
) : BaseAdapter<ItemInfoBinding,  WalletInfo>(list) {

    override fun bindItem(binding: ItemInfoBinding, item: WalletInfo) {
        binding.viewModel = InfoItemViewModel(item)
    }

    override fun getLayoutId() = R.layout.item_info

    interface Listener {
        fun selectItem(item: String)
    }
}