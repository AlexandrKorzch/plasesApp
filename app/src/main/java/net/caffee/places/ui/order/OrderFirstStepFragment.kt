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
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.ui.common.fragments.RecyclerViewFragment

class OrderFirstStepFragment : BaseFragment<FragmentOrderFirstStepBinding, OrderFirstStepViewModel>() {

    companion object {
        private const val TAG = "OrderFirstStepFragment"

        fun getInstance(listener: Listener, placeId: Long): OrderFirstStepFragment {
            val args = Bundle().apply { putLong(PLACE_ID, placeId) }
            return OrderFirstStepFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    private var placeId: Long = 0L

    override fun layoutResId() = R.layout.fragment_order_first_step

    override fun viewModelClass() = OrderFirstStepViewModel::class.java

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
        showMenuItem(this@OrderFirstStepFragment, cartItem, viewModel().cartCount, { showBasket() })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel().cartCount.removeObservers(this@OrderFirstStepFragment)
    }

    private fun showBasket() {
        OrderBasketDialog.getInstance(placeId)
                .show(fragmentManager, OrderBasketDialog.TAG)
    }

    override fun getViewModelHandler() = object : OrderFirstStepViewModel.Handler {
        override fun openChooser(type: String) {
            listener?.openChooser(type, categoryListener)
        }

        override fun openSecondStep() {
            listener?.openSecondStep(placeId)
        }
    }

    private val categoryListener = object : RecyclerViewFragment.CategoryListener {
        override fun changeCategory(item: BaseCategory) {


        }
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openChooser(cities: String, categoryListener: RecyclerViewFragment.CategoryListener)
        fun openSecondStep(placeId: Long)
    }
}