package net.caffee.places.ui.menu

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentMenuBinding
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.remote.model.ProdSubCategory
import net.caffee.places.ui.common.GridSpacingItemDecoration
import net.caffee.places.ui.menu.adapters.MenuAdapter

class MenuFragment : BaseFragment<FragmentMenuBinding, MenuViewModel>() {

    companion object {
        private const val TAG = "MenuFragment"
        private const val MENU_RECYCLER_VIEW_COLUMNS_COUNT = 2

        fun getInstance(listener: Listener, placeId: Long): MenuFragment {
            val args = Bundle().apply {
                putLong(PLACE_ID, placeId)
            }
            return MenuFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    private var placeId = 0L
    var listener: Listener? = null
    private var list = mutableListOf("")

    override fun layoutResId() = R.layout.fragment_menu

    override fun viewModelClass() = MenuViewModel::class.java

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
        listener?.toolbarTitle(getString(R.string.fragment_menu_title))
        setupAdapter()
        viewModel().loadData()
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = GridLayoutManager(activity, MENU_RECYCLER_VIEW_COLUMNS_COUNT)
        binding.recyclerView.adapter = MenuAdapter(mutableListOf(), menuAdapterListener)
        val marginInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_divider)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(
                MENU_RECYCLER_VIEW_COLUMNS_COUNT, marginInPixels, true, 0))
    }

    private fun openMenuCategories(subCategoryId: ProdSubCategory) {
        listener?.openMenuCategories(subCategoryId)
    }

    private val menuAdapterListener = object : MenuAdapter.Listener {
        override fun selectItem(subCategory: ProdSubCategory) {
            openMenuCategories(subCategory)
        }
    }

    override fun clearFields() {
        listener = null
    }

    override fun getViewModelHandler() = object : MenuViewModel.Handler{

    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openMenuCategories(subCategoryId: ProdSubCategory)
    }
}