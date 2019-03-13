package net.caffee.places.ui.place.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.FragmentPlaceBinding
import net.caffee.places.extensions.showMenuItem
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.remote.model.Comment
import net.caffee.places.repo.remote.model.Product
import net.caffee.places.ui.common.GridSpacingItemDecoration
import net.caffee.places.ui.menuitem.MenuItemDialog
import net.caffee.places.ui.place.adapters.PlaceMenuAdapter
import net.caffee.places.ui.place.adapters.PlacePagerAdapter
import net.caffee.places.ui.place.adapters.PlaceReviewAdapter

class PlaceFragment : BaseFragment<FragmentPlaceBinding, PlaceFragmentViewModel>() {

    companion object {
        private const val TAG = "PlaceFragment"
        private const val MENU_RECYCLER_VIEW_COLUMNS_COUNT = 2

        fun getInstance(listener: Listener, placeId: Long): PlaceFragment {
            val args = Bundle().apply { putLong(PLACE_ID, placeId) }
            return PlaceFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null
    override fun layoutResId() = R.layout.fragment_place
    override fun viewModelClass() = PlaceFragmentViewModel::class.java

    private var placeId: Long = 0L
    private lateinit var map: GoogleMap
    private lateinit var reviewAdapter: PlaceReviewAdapter
    private lateinit var placeDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModelEvents()
        getArgs()
    }

    private fun getArgs() {
        placeId = arguments?.getLong(PLACE_ID)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenuAdapter()
        setupReviewAdapter()
        setupMap()
        setReviewCallBack()
        initViewPager()
        getData()
    }

    private fun initViewPager() {
        binding.vpGallery.adapter = PlacePagerAdapter(childFragmentManager, mutableListOf())
        binding.vpGallery.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                binding.pageIndicatorView.selection = position;
            }
        })
    }

    private fun getData() {
        viewModel().getData(placeId)
    }

    private fun setReviewCallBack() {
        binding.fbAddReview.setOnClickListener {
            listener?.openNewReviewPage(placeId)
        }
    }

    private fun setupReviewAdapter() {
        reviewAdapter = PlaceReviewAdapter(binding, layoutInflater)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_place_fragment, menu)

        val cartItem = menu?.findItem(R.id.cartItem)
        val bookingItem = menu?.findItem(R.id.bookingItem)
        showMenuItem(this@PlaceFragment, cartItem, viewModel().cartCount, { listener?.openBaskets(it) })
        showMenuItem(this@PlaceFragment, bookingItem, viewModel().bookingCount, { listener?.openBooking() })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel().cartCount.removeObservers(this@PlaceFragment)
        viewModel().bookingCount.removeObservers(this@PlaceFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cartItem -> {
                openCartsFragment()
                return true
            }
        }
        return false
    }

    private fun viewModelEvents() {
        viewModel().onOpenLiveData.observe(this, Observer { type ->
            when (type) {
                PlaceFragmentViewModel.TYPE_RESERVATION -> openReservation()
                PlaceFragmentViewModel.EVENT_ON_OPEN_MENU -> openMenuFragment()
                PlaceFragmentViewModel.EVENT_ON_OPEN_COMPLAINTS -> openComplaintsFragment()
                PlaceFragmentViewModel.TYPE_DELIVERY -> openDelivery()
            }
        })
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style))
        showMarker()
        map.setOnMarkerClickListener(onMarkerClickListener)
    }

    private fun showMarker() {
        placeDisposable = viewModel().placeSubject
                .toFlowable(BackpressureStrategy.DROP)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    map.clear()
                    val latLong = LatLng(it.latitude, it.longitude)
                    map.addMarker(markerOptions(latLong))
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15F))
                }
    }

    private fun markerOptions(latLng: LatLng) =
            MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_unselected_pin_for_map))

    private val onMarkerClickListener = GoogleMap.OnMarkerClickListener { marker ->
        false
    }

    private fun setupMenuAdapter() {
        with(binding) {
            menuRecyclerView.layoutManager =
                    GridLayoutManager(activity, MENU_RECYCLER_VIEW_COLUMNS_COUNT)
            menuRecyclerView.isNestedScrollingEnabled = false
            menuRecyclerView.adapter =
                    PlaceMenuAdapter(mutableListOf(), placeMenuAdapterListener)
            val marginInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_divider)
            menuRecyclerView.addItemDecoration(GridSpacingItemDecoration(
                    MENU_RECYCLER_VIEW_COLUMNS_COUNT, marginInPixels, true, 0))
        }
    }

    override fun getViewModelHandler() = object : PlaceFragmentViewModel.Handler {
        override fun showToolbarTitle(title: String) {
            listener?.toolbarTitle(title)
        }

        override fun setPagerIndicatorSize(size: Int) {
            binding.pageIndicatorView.count = size
        }

        override fun showComments(comments: List<Comment>) {
            reviewAdapter.setItems(comments)
        }

        override fun onAddComments(comments: List<Comment>) {
            reviewAdapter.addItemsWithScroll(comments)
        }
    }

    private fun showMenuItem(productId: Long) {
        MenuItemDialog.getInstance(productId, placeId)
                .show(fragmentManager, MenuItemDialog.TAG)
    }

    private fun openCartsFragment() {
        listener?.openOneBasketFragment(placeId)
    }

    private fun openReservation() {
        listener?.openReservationFragment()
    }

    private fun openMenuFragment() {
        listener?.openMenuFragment()
    }

    private fun openComplaintsFragment() {
        listener?.openComplaintsFragment()
    }

    private fun openDelivery() {
        listener?.openDelivery()
    }

    private val placeMenuAdapterListener = object : PlaceMenuAdapter.Listener {
        override fun selectItem(product: Product) {
            showMenuItem(product.id)
        }
    }

    override fun clearFields() {
        listener = null
        placeDisposable.dispose()
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openOneBasketFragment(placeId: Long)
        fun openReservationFragment()
        fun openMenuFragment()
        fun openComplaintsFragment()
        fun openBaskets(baskets: Int)
        fun openBooking()
        fun openDelivery()
        fun openNewReviewPage(placeId: Long)
    }

    interface ViewModelHandler : BaseHandler
}