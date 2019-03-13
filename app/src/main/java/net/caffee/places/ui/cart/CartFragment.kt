package net.caffee.places.ui.cart

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentCartBinding
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.db.model.basket.Goods
import net.caffee.places.ui.cart.adapter.CartAdapter

class CartFragment
    : BaseFragment<FragmentCartBinding, CartViewModel>() {

    companion object {
        private const val TAG = "CartFragment"

        fun getInstance(listener: Listener, placeId: Long): CartFragment {
            val args = Bundle().apply {
                putLong(PLACE_ID, placeId)
            }
            return CartFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    private var placeId: Long = 0L

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_cart

    override fun viewModelClass() = CartViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    private fun getArgs() {
        placeId = arguments?.getLong(PLACE_ID)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.busket))
        setupAdapter()
        viewModel().getData(placeId)
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = CartAdapter(mutableListOf(), cartAdapterListener)
    }

    private val cartAdapterListener = object : CartAdapter.Listener {

        override fun changeCount(item: Goods) {
            viewModel().update(item)
        }

        override fun selectItem(item: String) {
            // TODO("not implemented")
        }
    }

    override fun clearFields() {
        listener = null
    }

    override fun getViewModelHandler() = object : CartViewModel.Handler {
        override fun orderClick(){
            listener?.orderClick(placeId)
        }
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun orderClick(placeId: Long)
    }
}