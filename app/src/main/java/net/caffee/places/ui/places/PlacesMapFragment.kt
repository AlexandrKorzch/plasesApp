package net.caffee.places.ui.places

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.FragmentPlacesMapBinding
import net.caffee.places.databinding.PopupWindowListOrMapBinding
import net.caffee.places.extensions.initPopap
import net.caffee.places.extensions.showMenuItem
import net.caffee.places.extensions.toast
import net.caffee.places.repo.remote.model.Place
import net.caffee.places.ui.common.PopupListOrMapViewModel
import net.caffee.places.util.logWithTAG


class PlacesMapFragment : BaseFragment<FragmentPlacesMapBinding, PlacesViewModel>() {

    companion object {
        private const val TAG = "PlacesMapFragment"
        const val DATA_TYPE_KEY = "DATA_TYPE_KEY"
        const val ALL_PLACES = 0
        const val FAVORITE_PLACES = 1
        const val PLACES_WITH_DELIVERY = 2
        const val PLACES_NEAR_YOU = 3

        fun getInstance(listener: Listener, dataType: Int): PlacesMapFragment {
            val args = Bundle().apply {
                putInt(DATA_TYPE_KEY, dataType)
            }
            return PlacesMapFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null
    private var type: Int = 0
    private var map: GoogleMap? = null
    private var popupWindow: PopupWindow? = null
    private val markerList = ArrayList<Marker>()
    private lateinit var placesDisposable: Disposable

    override fun layoutResId() = R.layout.fragment_places_map

    override fun viewModelClass() = PlacesViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments.let {
            getData(arguments?.getInt(PlacesFragment.DATA_TYPE_KEY))
        }
    }

    private fun getData(dataType: Int?) {
        type = dataType!!
        when (dataType) {
            ALL_PLACES -> {
                logWithTAG("ALL_PLACES")
            }
            FAVORITE_PLACES -> {
                logWithTAG("FAVORITE_PLACES")
            }
            PLACES_WITH_DELIVERY -> {
                logWithTAG("PLACES_WITH_DELIVERY")
            }
            PLACES_NEAR_YOU -> {
                logWithTAG("PLACES_NEAR_YOU")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        toolBarTitle()
    }

    private fun setTextToSearch() {
        if (!viewModel().searchText.isEmpty()) {
            binding.svSearch.setQuery(viewModel().searchText, false)
            binding.svSearch.isIconified = false
            hideSoftKeyboard(binding.svSearch)
        }
    }

    private fun toolBarTitle() {
        when (type) {
            PlacesFragment.ALL_PLACES -> {
                listener?.toolbarTitle(getString(R.string.place_all))
            }
            PlacesFragment.FAVORITE_PLACES -> {
                listener?.toolbarTitle(getString(R.string.place_favorite))
            }
            PlacesFragment.PLACES_WITH_DELIVERY -> {
                listener?.toolbarTitle(getString(R.string.place_delivery))
            }
            PlacesFragment.PLACES_NEAR_YOU -> {
                listener?.toolbarTitle(getString(R.string.place_near_you))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_places_map_fragment, menu)

        val cartItem = menu?.findItem(R.id.cartItem)
        val bookingItem = menu?.findItem(R.id.bookingItem)
        showMenuItem(this@PlacesMapFragment, cartItem, viewModel().cartCount, { listener?.openBaskets() })
        showMenuItem(this@PlacesMapFragment, bookingItem, viewModel().bookingCount, { listener?.openBooking() })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.modeItem -> {
                val view = activity?.findViewById<View>(R.id.modeItem)
                if (view != null) {
                    showPopupWindowListOrMap(view)
                } else {
                    listener?.openPlacesFragment(type)
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

    private fun setupUi() {
        setupMap()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private fun showPopupWindowListOrMap(view: View?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<PopupWindowListOrMapBinding>(
                inflater, R.layout.popup_window_list_or_map, null, false)
        binding.viewModel = PopupListOrMapViewModel(false, popupWindowViewModelListener)
        popupWindow = initPopap(PopupWindow(activity), binding, view)
    }

    private fun markerOptions(id: Long, latLng: LatLng) =
            MarkerOptions()
                    .snippet(id.toString())
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_unselected_pin_for_map))

    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map?.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style))
        map?.let { showMarkers() }
        map?.setOnMarkerClickListener(onMarkerClickListener)
        setTextToSearch()
    }

    private fun showMarkers() {
        placesDisposable = viewModel().listSubject
                .toFlowable(BackpressureStrategy.DROP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    clearMap()
                    if (it.size == 1 && it.first().id == 0L) return@subscribe
                    addNewMarkers(it)
                    moveCamera(it)
                    showBage(it)
                })
    }

    private fun clearMap() {
        map?.clear()
        markerList.clear()
    }

    private fun addNewMarkers(places: List<Place>) {
        places.forEach {
            val marker = map?.addMarker(markerOptions(it.id, LatLng(it.latitude, it.longitude)))
            markerList.add(marker!!)
        }
    }

    private fun moveCamera(places: List<Place>) {
        if (places.isNotEmpty()) {
            val builder = LatLngBounds.Builder()
            markerList.forEach { builder.include(it.position) }
            val bounds = builder.build()
            map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
        }
    }

    private fun showBage(it: List<Place>) {
        if (it.size == 1) {
            viewModel().showPlaceBage(it.first().id)
            markerList.first()
                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_selected_pin_for_map))
        } else {
            viewModel().bageVisible.set(false)
        }
    }

    private val onMarkerClickListener = GoogleMap.OnMarkerClickListener { marker ->
        viewModel().showPlaceBage(marker.snippet.toLong())
        markerList.forEach {
            it.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_unselected_pin_for_map))
        }
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_selected_pin_for_map))
        true
    }

    private val popupWindowViewModelListener = object : PopupListOrMapViewModel.Listener {
        override fun openListView() {
            popupWindow?.dismiss()
            listener?.openPlacesFragment(type)
        }

        override fun openMapView() {
            // TODO remove toast
            popupWindow?.dismiss()
            activity?.toast("mapStr?Item")
        }
    }

    override fun getViewModelHandler() = object : PlacesViewModel.Handler {
    }

    override fun clearFields() {
        listener = null
        map = null
        popupWindow = null
        placesDisposable.dispose()
    }

    interface Handler : BaseHandler {
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openFilter()
        fun openBaskets()
        fun openBooking()
        fun openPlacesFragment(dataType: Int)
        fun openPlace(placeId: Long)
    }

    interface ViewModelHandler : BaseHandler {

    }
}