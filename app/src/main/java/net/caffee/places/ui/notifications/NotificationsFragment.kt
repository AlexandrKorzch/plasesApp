package net.caffee.places.ui.notifications

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentNotificationsBinding
import net.caffee.places.extensions.convertDpToPixel
import net.caffee.places.extensions.toast
import net.caffee.places.ui.common.ItemOffsetDecoration
import net.caffee.places.ui.notifications.adapter.NotificationsAdapter

class NotificationsFragment
    : BaseFragment<FragmentNotificationsBinding, NotificationsViewModel>() {

    companion object {
        private const val TAG = "NotificationsFragment"

        fun getInstance(listener: Listener): NotificationsFragment {
            val args = Bundle().apply { }
            return NotificationsFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null
    private lateinit var newNotificationsTextView: TextView

    override fun layoutResId() = R.layout.fragment_notifications

    override fun viewModelClass() = NotificationsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.main_notification))
        setupUi()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_notifications_fragment, menu)

        val newNotifications = menu?.findItem(R.id.newNotifications)
        val newNotificationsContainer = newNotifications?.actionView as FrameLayout
        newNotificationsTextView = newNotificationsContainer
                .findViewById(R.id.newNotificationsTextView) as TextView
        // TODO remove (example)
        //newNotificationsTextView.text = "1"
        //newNotificationsTextView.visibility = View.GONE

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.newNotifications -> {
                // TODO remove toast
                activity?.toast("newNotifications")
                return true
            }
        }
        return false
    }

    private fun setupUi() {
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.addItemDecoration(setupItemOffsetDecoration())
        binding.recyclerView.adapter =
                NotificationsAdapter(viewModel().list(), notificationsAdapterListener)
    }

    private fun setupItemOffsetDecoration() =
            ItemOffsetDecoration(
                    viewModel().offsetPosition(),
                    activity?.convertDpToPixel(NotificationsViewModel.NEW_NOTIFICATION_ITEM_OFFSET)
            )

    private val notificationsAdapterListener = object : NotificationsAdapter.Listener {
        override fun openNearYou(item: String) {
            listener?.openPlacesNearYou()
        }

        override fun selectItem(item: String) {
            listener?.openBooking(123L)
        }
    }

    override fun getViewModelHandler() = object : NotificationsViewModel.Handler {
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openBooking(placeId: Long)
        fun openPlacesNearYou()
    }
}