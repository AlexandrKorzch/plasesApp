package net.caffee.places.ui.places

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.PopupWindow
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.FragmentPlacesBinding
import net.caffee.places.databinding.PopupWindowListOrMapBinding
import net.caffee.places.extensions.initPopap
import net.caffee.places.extensions.showMenuItem
import net.caffee.places.extensions.toast
import net.caffee.places.ui.common.PopupListOrMapViewModel
import net.caffee.places.ui.places.adapter.PlacesAdapter

class PlacesFragment : BaseFragment<FragmentPlacesBinding, PlacesViewModel>() {

    companion object {
        private const val TAG = "PlacesFragment"
        const val DATA_TYPE_KEY = "DATA_TYPE_KEY"
        const val ALL_PLACES = 0
        const val FAVORITE_PLACES = 1
        const val PLACES_WITH_DELIVERY = 2
        const val PLACES_NEAR_YOU = 3

        fun getInstance(listener: Listener, dataType: Int): PlacesFragment {
            val args = Bundle().apply {
                putInt(DATA_TYPE_KEY, dataType)
            }
            return PlacesFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    private var popupWindow: PopupWindow? = null

    override fun layoutResId() = R.layout.fragment_places

    override fun viewModelClass() = PlacesViewModel::class.java

    private var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments.let {type = arguments?.getInt(DATA_TYPE_KEY)!!}
        viewModel().setPlacesType(type)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBarTitle()
        setupAdapter()
//        setTextToSearch()
    }

//    private fun setTextToSearch() { // todo check does it work correctly?
//        if (!viewModel().searchText.isEmpty()) {
//            binding.svSearch.setQuery(viewModel().searchText, false)
//            binding.svSearch.isIconified = false
//            hideSoftKeyboard(binding.svSearch)
//        }
//    }

    private fun toolBarTitle() {
        when (type) {
            ALL_PLACES ->{listener?.toolbarTitle(getString(R.string.place_all))}
            FAVORITE_PLACES ->{listener?.toolbarTitle(getString(R.string.place_favorite))}
            PLACES_WITH_DELIVERY ->{listener?.toolbarTitle(getString(R.string.place_delivery))}
            PLACES_NEAR_YOU ->{listener?.toolbarTitle(getString(R.string.place_near_you))}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_places_fragment, menu)
        val cartItem = menu?.findItem(R.id.cartItem)
        val bookingItem = menu?.findItem(R.id.bookingItem)
        showMenuItem(this@PlacesFragment, cartItem, viewModel().cartCount, { listener?.openBaskets() })
        showMenuItem(this@PlacesFragment, bookingItem, viewModel().bookingCount, { listener?.openBooking() })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.modeItem -> {
                val view = activity?.findViewById<View>(R.id.modeItem)
                if (view != null) {
                    showPopupWindowListOrMap(view)
                } else {
                    listener?.showPlacesMapFragment(type)
                }
                return true
            }
            R.id.filterItem -> {
                listener?.openFilter()
                return true
            }
        }
        return false
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = PlacesAdapter(viewModel().list, placesAdapterListener)
    }

    private fun showPopupWindowListOrMap(view: View?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<PopupWindowListOrMapBinding>(
                inflater, R.layout.popup_window_list_or_map, null, false)
        binding.viewModel = PopupListOrMapViewModel(true, popupWindowViewModelListener)
        popupWindow = initPopap(PopupWindow(activity), binding, view)
    }

    override fun getViewModelHandler() = object : PlacesViewModel.Handler {
    }

    private val placesAdapterListener = object : PlacesAdapter.Listener {

        override fun openBooking(placeId: Long) {
            listener?.openBooking(placeId)
        }

        override fun openDelivery(placeId: Long) {
            listener?.openDelivery(placeId)
        }

        override fun setFavoritePlace(placeId: Long, favorite: Boolean) {
            viewModel().setFavoritePlace(placeId, favorite)
        }

        override fun openPlace(placeId: Long) {
            listener?.openPlace(placeId)
        }
    }

    private val popupWindowViewModelListener = object : PopupListOrMapViewModel.Listener {
        override fun openListView() {
            // TODO remove toast
            activity?.toast("listItem")
            popupWindow?.dismiss()
        }

        override fun openMapView() {
            listener?.showPlacesMapFragment(type)
            popupWindow?.dismiss()
        }
    }

    override fun clearFields() {
        listener = null
        popupWindow = null
    }

    fun request() {
        viewModel().onResume()
    }

    interface Handler : BaseHandler {
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun showPlacesMapFragment(dataType: Int)
        fun openBaskets()
        fun openBooking()
        fun openBooking(placeId: Long)
        fun openFilter()
        fun openPlace(placeId: Long)
        fun openDelivery(placeId: Long)
    }

    interface ViewModelHandler : BaseHandler
}