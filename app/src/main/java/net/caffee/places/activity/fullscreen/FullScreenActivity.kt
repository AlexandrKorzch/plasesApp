package net.caffee.places.activity.fullscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import net.caffee.places.R
import net.caffee.places.activity.common.CommonActivity
import net.caffee.places.activity.flat.FlatToolBarActivity
import net.caffee.places.activity.flat.FlatToolBarActivity.Companion.BOOKING_TYPE
import net.caffee.places.activity.flat.FlatToolBarActivity.Companion.PAYMENTS_TYPE
import net.caffee.places.activity.menutitle.CustomToolBarActivity
import net.caffee.places.architecture.BaseActivity
import net.caffee.places.databinding.ActivityFullScreenBinding
import net.caffee.places.extensions.replaceFragment
import net.caffee.places.global.PLACE_ID
import net.caffee.places.ui.cart.CartFragment
import net.caffee.places.ui.profile.ProfileFragment


class FullScreenActivity : BaseActivity<ActivityFullScreenBinding, FullScreenActivityViewModel>() {

    companion object {
        const val TAG = "FullScreenActivity"
        private const val FRAGMENT_CONTAINER = R.id.fragmentContainer
        const val EDIT_PROFILE_TYPE = 1
        const val ONE_BASKET_TYPE = 2

        private const val EXTRA_SCREEN_TYPE =
                "net.vjet.myplace.activity.fullscreen.FullScreenActivity.screenType"

        fun newIntent(context: Context?, screenType: Int) =
                Intent(context, FullScreenActivity::class.java).apply {
                    putExtra(EXTRA_SCREEN_TYPE, screenType)
                }
    }

    override fun layoutResId() = R.layout.activity_full_screen

    override fun viewModelClass() = FullScreenActivityViewModel::class.java

    private var placeId: Long = 0L

    private var screenType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extra()
        setupUi()
    }

    private fun extra() {
        screenType = intent.getIntExtra(EXTRA_SCREEN_TYPE, screenType)
        placeId = intent.getLongExtra(PLACE_ID, placeId)
    }

    private fun setupUi() {
        setupToolbar()
        when (screenType) {
            EDIT_PROFILE_TYPE -> openProfileFragment()
            ONE_BASKET_TYPE -> openOneBasketFragment(placeId)
        }
    }

    private fun setupToolbar() {
        setupToolbar(binding.toolbar, true, R.drawable.ic_arrow_back_white)
        binding.toolbar.setTitleTextColor(
                ContextCompat.getColor(this@FullScreenActivity, R.color.colorWhite))
        setStatusBarTranslucent(true)
        removeAppBarOutline()
    }

    private fun openOneBasketFragment(placeId: Long) {
        replaceFragment(
                FRAGMENT_CONTAINER,
                CartFragment.getInstance(cartFragmentListener, placeId)
        )
    }

    private fun openProfileFragment() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                ProfileFragment.getInstance(profileFragmentListener)
        )
    }

    private val profileFragmentListener = object : ProfileFragment.Listener {
        override fun openWallet() {
            startActivity(
                    CommonActivity.newIntent(this@FullScreenActivity, CommonActivity.TYPE_WALLET)
            )
        }

        override fun openPayments() {
            startActivity(
                    FlatToolBarActivity.newIntent(this@FullScreenActivity, PAYMENTS_TYPE)
            )
        }

        override fun openBooking() {
            startActivity(
                    FlatToolBarActivity.newIntent(this@FullScreenActivity, BOOKING_TYPE)
            )
        }

        override fun openEditProfile() {
            startActivity(
                    CustomToolBarActivity.newIntent(
                            this@FullScreenActivity, CustomToolBarActivity.EDIT_PROFILE))
        }

        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val cartFragmentListener = object : CartFragment.Listener {

        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }

        override fun orderClick(placeId: Long) {
            startActivity(FlatToolBarActivity.newIntent(this@FullScreenActivity,
                    FlatToolBarActivity.ORDER_FIRST_STEP_TYPE).apply { putExtra(PLACE_ID , placeId) }
            )
        }
    }

    override fun getViewModelHandler() = object : FullScreenActivityViewModel.Handler {
    }
}