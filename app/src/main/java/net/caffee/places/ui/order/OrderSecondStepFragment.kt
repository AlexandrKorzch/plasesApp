package net.caffee.places.ui.order

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentOrderFirstStepBinding
import net.caffee.places.extensions.showMenuItem
import net.caffee.places.global.PLACE_ID


class OrderSecondStepFragment : BaseFragment<FragmentOrderFirstStepBinding, OrderSecondStepViewModel>() {

    companion object {
        private const val TAG = "OrderSecondStepFragment"

        fun getInstance(listener: Listener, placeId: Long): OrderSecondStepFragment {
            val args = Bundle().apply { putLong(PLACE_ID, placeId) }
            return OrderSecondStepFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    private var placeId: Long = 0L

    override fun layoutResId() = R.layout.fragment_order_second_step

    override fun viewModelClass() = OrderSecondStepViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getArgs()
    }

    private fun getArgs() {
        placeId = arguments?.getLong(PLACE_ID)!!
        viewModel().setPlaceId(placeId)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.order_order))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_order, menu)
        val cartItem = menu?.findItem(R.id.cartItem)
        showMenuItem(this@OrderSecondStepFragment, cartItem, viewModel().cartCount, { showBasket() })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel().cartCount.removeObservers(this@OrderSecondStepFragment)
    }

    private fun showBasket() {
        OrderBasketDialog.getInstance(placeId)
                .show(fragmentManager, OrderBasketDialog.TAG)
    }

    override fun getViewModelHandler() = object : OrderSecondStepViewModel.Handler {
        override fun openOrderThirdStep() {
            listener?.openOrderThirdStep(placeId)
        }
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openOrderThirdStep(placeId: Long)
    }
}