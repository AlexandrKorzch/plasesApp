package net.caffee.places.activity.flat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import net.caffee.places.R
import net.caffee.places.activity.main.MainActivity
import net.caffee.places.architecture.BaseActivity
import net.caffee.places.databinding.ActivityFlatToolbarBinding
import net.caffee.places.extensions.replaceFragment
import net.caffee.places.global.PLACE_ID
import net.caffee.places.ui.booking.BookingFragment
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.ui.order.OrderFirstStepFragment
import net.caffee.places.ui.order.OrderSecondStepFragment
import net.caffee.places.ui.order.OrderThirdStepFragment
import net.caffee.places.ui.payments.PaymentsFragment

class FlatToolBarActivity : BaseActivity<ActivityFlatToolbarBinding, FlatToolBarViewModel>() {

    var backToMain = false

    companion object {
        private const val TAG = "FlatToolBarActivity"
        const val ORDER_FIRST_STEP_TYPE = 1
        const val PAYMENTS_TYPE = 2
        const val BOOKING_TYPE = 3
        private const val FRAGMENT_CONTAINER = R.id.fragmentContainer
        private const val EXTRA_SCREEN_TYPE =
                "net.vjet.myplace.activity.flat.FlatToolBarActivity.screenType"

        fun newIntent(context: Context?, screenType: Int) =
                Intent(context, FlatToolBarActivity::class.java).apply {
                    putExtra(EXTRA_SCREEN_TYPE, screenType)
                }
    }

    private var screenType = ORDER_FIRST_STEP_TYPE

    private var placeId: Long = 0L

    override fun layoutResId() = R.layout.activity_flat_toolbar

    override fun viewModelClass() = FlatToolBarViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extra()
        setupUi()
    }

    private fun extra() {
        screenType = intent.getIntExtra(EXTRA_SCREEN_TYPE, ORDER_FIRST_STEP_TYPE)
        placeId = intent.getLongExtra(PLACE_ID, placeId)
    }

    private fun setupUi() {
        setupToolbar(binding.toolbarFl, true, R.drawable.ic_arrow_back_black)
        removeAppBarOutline()
        when (screenType) {
            ORDER_FIRST_STEP_TYPE -> openOrderFirstStepFragment(placeId)
            PAYMENTS_TYPE -> openPaymentsFragment()
            BOOKING_TYPE -> openBookingFragment()
        }
    }

    private fun openBookingFragment() {
        replaceFragment(FRAGMENT_CONTAINER,
                BookingFragment.getInstance(bookingFragmentListener)
        )
    }

    private fun openPaymentsFragment() {
        replaceFragment(FRAGMENT_CONTAINER,
                PaymentsFragment.getInstance(paymentsFragmentListener)
        )
    }

    private fun openOrderFirstStepFragment(placeId: Long) {
        replaceFragment(FRAGMENT_CONTAINER,
                OrderFirstStepFragment.getInstance(orderFirstStepFragmentListener, placeId)
        )
    }

    private val orderFirstStepFragmentListener = object : OrderFirstStepFragment.Listener {
        override fun openSecondStep(placeId: Long) {
            replaceFragment(
                    FRAGMENT_CONTAINER,
                    OrderSecondStepFragment.getInstance(orderSecondStepFragmentListener, placeId),
                    true, "", true
            )
        }

        override fun openChooser(categoryName: String,
                                 categoryListener: RecyclerViewFragment.CategoryListener) {
            replaceFragment(
                    FRAGMENT_CONTAINER,
                    RecyclerViewFragment.getInstance(
                            recyclerViewFragmentListener, categoryListener, -1, categoryName),
                    true, "", true
            )
        }

        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val orderSecondStepFragmentListener = object : OrderSecondStepFragment.Listener {
        override fun openOrderThirdStep(placeId: Long) {
            replaceFragment(FRAGMENT_CONTAINER,
                    OrderThirdStepFragment.getInstance(orderThirdStepFragmentListener, placeId),
                    true, "", true)
        }

        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val paymentsFragmentListener = object : PaymentsFragment.Listener {
    }

    private val bookingFragmentListener = object : BookingFragment.Listener {
    }

    private val orderThirdStepFragmentListener = object : OrderThirdStepFragment.Listener {
        override fun overrideBackPressed() {
            backToMain = true
        }

        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val recyclerViewFragmentListener = object : RecyclerViewFragment.Listener {
        override fun getComplaintText(item: String) = onBackPressed()
        override fun showNavigationDrawerToolbarButton() {// TODO("not implemented")
        }

        override fun showBackToolbarButton() {// TODO("not implemented")
        }
    }


    override fun onBackPressed() {
        if (backToMain) {
            startActivity(MainActivity.newIntent(this@FlatToolBarActivity)
                    .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
        } else {
            super.onBackPressed()
        }
    }

    override fun getViewModelHandler() = object : FlatToolBarViewModel.Handler {
    }
}