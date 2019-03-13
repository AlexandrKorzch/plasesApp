package net.caffee.places.ui.carts

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentCartsBinding
import net.caffee.places.repo.db.model.basket.Basket
import net.caffee.places.ui.carts.adapter.CartsAdapter

class CartsFragment
    : BaseFragment<FragmentCartsBinding, CartsViewModel>() {

    companion object {
        private const val TAG = "CartsFragment"

        fun getInstance(listener: Listener): CartsFragment {
            val args = Bundle().apply { }
            return CartsFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_carts

    override fun viewModelClass() = CartsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.buskets))
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = CartsAdapter(mutableListOf(), cartsAdapterListener)
    }

    private val cartsAdapterListener = object : CartsAdapter.Listener {
        override fun selectItem(item: Basket) {
            listener?.openOneBasket(item)
        }
    }

    override fun clearFields() {
        listener = null
    }

    override fun getViewModelHandler() = object : CartsViewModel.Handler {
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openOneBasket(item: Basket)
    }
}