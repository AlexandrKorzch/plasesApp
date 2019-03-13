package net.caffee.places.ui.booking

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentBookingCurrentBinding

class BookingCurrentFragment : BaseFragment<FragmentBookingCurrentBinding, BookingCurrentViewModel>() {

    companion object {
        private const val TAG = "BookingCurrentFragment"
        const val TYPE_KEY = "type"

        fun getInstance(listener: Listener, screenType: Int): BookingCurrentFragment {
            val args = Bundle().apply {}
            args.putInt(TYPE_KEY, screenType)
            return BookingCurrentFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    private var type: Int = -1

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_booking_current

    override fun viewModelClass() = BookingCurrentViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_booking_fragment, menu)
        val searchView = menu?.findItem(R.id.searchItem)?.actionView as SearchView
        searchView.setOnQueryTextListener(viewModel().searchChangeListener)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun arguments() {
        type = arguments?.getInt(TYPE_KEY) ?: -1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        viewModel().getBookingRequest(type)
    }

    private fun setupListAdapter() {
        binding.rvItems.layoutManager = LinearLayoutManager(context)
        binding.rvItems.adapter = BookingAdapter(type, mutableListOf(), object : BookingAdapter.Listener {
            override fun selectItem(bookingId: Long) {
                listener?.selectItem(bookingId)
            }
        })
    }

    override fun getViewModelHandler() = object : BookingPastViewModel.Handler {
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun selectItem(bookingId: Long)
    }
}