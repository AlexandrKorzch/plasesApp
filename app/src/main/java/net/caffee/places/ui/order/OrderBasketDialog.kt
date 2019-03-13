package net.caffee.places.ui.order

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.databinding.DialogBasketBinding
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.db.model.basket.Goods
import net.caffee.places.ui.cart.adapter.CartAdapter


class OrderBasketDialog : BaseFragmentDialog<DialogBasketBinding, OrderBasketViewModel>() {

    companion object {
        const val TAG = "OrderBasketDialog"

        fun getInstance(placeId: Long): OrderBasketDialog {
            val args = Bundle().apply {
                putLong(PLACE_ID, placeId)
            }
            return OrderBasketDialog().apply { arguments = args }
        }
    }

    private var placeId: Long = 0L

    override fun layoutResId() = R.layout.dialog_basket

    override fun viewModelClass() = OrderBasketViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog)
        getArgs()
    }

    private fun getArgs() {
        placeId = arguments?.getLong(PLACE_ID)!!
    }
    override fun onStart() {
        super.onStart()
        dialog?.let {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.setGravity(Gravity.TOP)
            val attributes = dialog.window.attributes
            val marginTop = 105
            attributes.y = marginTop
            dialog.window.attributes = attributes
        }

        binding.tvNext.setOnClickListener { dismiss() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        viewModel().getData(placeId)
    }

    private fun setupAdapter() {
        binding.rvItems.layoutManager = LinearLayoutManager(activity)
        binding.rvItems.adapter = CartAdapter(mutableListOf(), cartAdapterListener)
    }

    private val cartAdapterListener = object : CartAdapter.Listener {

        override fun changeCount(item: Goods) {
            viewModel().update(item)
        }

        override fun selectItem(item: String) {
            // TODO("not implemented")
        }
    }

    override fun getViewModelHandler() = object: OrderBasketViewModel.Handler{
    }

    override fun clearFields() {
    }
}