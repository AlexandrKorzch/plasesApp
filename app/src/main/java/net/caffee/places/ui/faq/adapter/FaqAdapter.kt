package net.caffee.places.ui.faq.adapter

import net.caffee.places.R
import net.caffee.places.databinding.ItemFaqBinding
import net.caffee.places.repo.remote.model.Fag
import net.caffee.places.view.baseview.BaseAdapter


class FaqAdapter(list: MutableList<Fag>?) : BaseAdapter<ItemFaqBinding, Fag>(list) {

    override fun getLayoutId() = R.layout.item_faq

    override fun bindItem(binding: ItemFaqBinding, item: Fag) {
        binding.item = FaqItem(item)
    }
}