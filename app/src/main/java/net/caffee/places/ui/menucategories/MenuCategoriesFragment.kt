package net.caffee.places.ui.menucategories

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentMenuCategoriesBinding
import net.caffee.places.extensions.showMenuItem
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.remote.model.Product
import net.caffee.places.ui.common.GridSpacingItemDecoration
import net.caffee.places.ui.menuitem.MenuItemDialog
import net.caffee.places.ui.place.adapters.PlaceMenuAdapter

class MenuCategoriesFragment
    : BaseFragment<FragmentMenuCategoriesBinding, MenuCategoriesViewModel>() {

    companion object {
        private const val TAG = "MenuCategoriesFragment"
        private const val RECYCLER_VIEW_COLUMNS_COUNT = 2
        private const val SUB_CATEGORY_ID = "SUB_CATEGORY_ID"
        private const val TITLE = "TITLE"

        fun getInstance(listener: Listener, subCategoryId: Long, placeId: Long, title: String): MenuCategoriesFragment {
            val args = Bundle().apply {
                putLong(SUB_CATEGORY_ID, subCategoryId)
                putLong(PLACE_ID, placeId)
                putString(TITLE, title)
            }
            return MenuCategoriesFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    private var subCategoryId = 0L
    private var placeId = 0L

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_menu_categories

    override fun viewModelClass() = MenuCategoriesViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getArgs()
    }

    private fun getArgs() {
        listener?.toolbarTitle(arguments?.getString(TITLE)!!)
        subCategoryId = arguments?.getLong(SUB_CATEGORY_ID)!!
        placeId = arguments?.getLong(PLACE_ID)!!
        viewModel().setIds(subCategoryId, placeId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_categories_fragment, menu)
        viewModel().createEvents()
        val cartItem = menu?.findItem(R.id.cartItem)
        val bookingItem = menu?.findItem(R.id.bookingItem)
        showMenuItem(this@MenuCategoriesFragment, cartItem, viewModel().cartCount) { listener?.openBaskets() }
        showMenuItem(this@MenuCategoriesFragment, bookingItem, viewModel().bookingCount) { listener?.openBooking() }
        viewModel().getBasketsCount()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPause() {
        super.onPause()
        viewModel().cartCount.call()
        viewModel().bookingCount.call()
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = GridLayoutManager(activity, RECYCLER_VIEW_COLUMNS_COUNT)
        binding.recyclerView.adapter = PlaceMenuAdapter(mutableListOf(), placeMenuAdapterListener)
        val marginInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_divider)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(
                RECYCLER_VIEW_COLUMNS_COUNT, marginInPixels, true, 0))
    }

    private fun showMenuItem(productId: Long) {
        MenuItemDialog.getInstance(productId, placeId)
                .show(fragmentManager, MenuItemDialog.TAG)
    }

    private val placeMenuAdapterListener = object : PlaceMenuAdapter.Listener {
        override fun selectItem(product: Product) {
            showMenuItem(product.id)
        }
    }

    override fun getViewModelHandler() = object : MenuCategoriesViewModel.Handler {
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openBaskets()
        fun openBooking()
    }
}