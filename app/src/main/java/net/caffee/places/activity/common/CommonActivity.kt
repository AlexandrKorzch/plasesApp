package net.caffee.places.activity.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import net.caffee.places.R
import net.caffee.places.activity.flat.FlatToolBarActivity
import net.caffee.places.activity.login.LoginActivity
import net.caffee.places.activity.menutitle.CustomToolBarActivity
import net.caffee.places.activity.place.PlaceActivity
import net.caffee.places.architecture.BaseActivity
import net.caffee.places.databinding.ActivityCommonBinding
import net.caffee.places.extensions.replaceFragment
import net.caffee.places.extensions.replaceFragmentFromBottom
import net.caffee.places.global.PLACE_ID
import net.caffee.places.ui.advantages.AdvantagesFragment
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.ui.complaints.ComplaintsFragment
import net.caffee.places.ui.places.PlacesFragment
import net.caffee.places.ui.places.PlacesMapFragment
import net.caffee.places.ui.promotion.PromotionFragment
import net.caffee.places.ui.termsandconditions.TermsAndConditionsFragment
import net.caffee.places.ui.wallet.WalletFragment
import net.caffee.places.ui.wallet.info.InfoFragment


class CommonActivity : BaseActivity<ActivityCommonBinding, CommonActivityViewModel>() {

    companion object {
        private const val TAG = "CommonActivity"
        const val EDIT_PROFILE_FRAGMENT_TAG = "EditProfileFragment"
        const val PROMOTION_ID = "PROMOTION_ID"
        const val PICK_IMAGE_REQUEST_CODE = 234
        const val TYPE_TERMS_AND_CONDITIONS = 0
        const val TYPE_PLACES_NEAR_YOU = 1
        const val TYPE_PROMOTION = 2
        const val TYPE_ADVANTAGES = 3
        const val TYPE_WALLET = 6
        private const val FRAGMENT_CONTAINER = R.id.fragmentContainer
        private const val EXTRA_SCREEN_TYPE =
                "net.vjet.myplace.activity.common.CommonActivity.screenType"

        fun newIntent(context: Context?, screenType: Int, placeId : Long = 0) =
                Intent(context, CommonActivity::class.java).apply {
                    putExtra(EXTRA_SCREEN_TYPE, screenType)
                    putExtra(PLACE_ID, placeId)
                }
    }

    private var screenType = TYPE_TERMS_AND_CONDITIONS
    private var placeId = 0L
    private var promotionId = 0L

    override fun layoutResId() = R.layout.activity_common

    override fun viewModelClass() = CommonActivityViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extra()
        setupUi()
    }

    private fun extra() {
        screenType = intent.getIntExtra(EXTRA_SCREEN_TYPE, TYPE_TERMS_AND_CONDITIONS)
        placeId = intent.getLongExtra(PLACE_ID, placeId)
        promotionId = intent.getLongExtra(PROMOTION_ID, placeId)
    }

    private fun setupUi() {
        setupToolbar(binding.toolbar, true, R.drawable.ic_arrow_back_black)
        when (screenType) {
            TYPE_TERMS_AND_CONDITIONS -> openTermsAndConditionsFragment()
            TYPE_PLACES_NEAR_YOU -> openPlacesNearYouFragment()
            TYPE_PROMOTION -> openPromotionFragment(promotionId)
            TYPE_ADVANTAGES -> openAdvantagesFragment()
            TYPE_WALLET -> openWalletFragment()
        }
    }

    private fun toolbarTitle(title: String, appBarOutline : Boolean){
        supportActionBar?.title = title
        if(appBarOutline) showAppBarOutline()
        else removeAppBarOutline()
    }

    private fun openWalletFragment() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                WalletFragment.getInstance(walletFragmentListener)
        )
    }

    private fun openTermsAndConditionsFragment() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                TermsAndConditionsFragment.getInstance(termsAndConditionsFragmentListener)
        )
    }

    private fun openPlacesNearYouFragment() {
        replaceFragmentFromBottom(FRAGMENT_CONTAINER,
                PlacesFragment.getInstance(placesFragmentListener, PlacesFragment.PLACES_NEAR_YOU))
    }
    
    private fun openPlacesMapFragment(type: Int) {
        replaceFragmentFromBottom(FRAGMENT_CONTAINER,
                PlacesMapFragment.getInstance(placesMapFragmentListener, type))
    }

    private fun openPromotionFragment(promotionId: Long) {
        replaceFragment(
                FRAGMENT_CONTAINER,
                PromotionFragment.getInstance(promotionFragmentListener, promotionId)
        )
    }

    private fun openAdvantagesFragment() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                AdvantagesFragment.getInstance(advantagesFragmentListener)
        )
    }

    private fun openComplaintsFragment() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                ComplaintsFragment.getInstance(complaintsFragmentListener, placeId),
                true
        )
    }

    private fun openPlacesFragment(dataType: Int) {
        replaceFragmentFromBottom(FRAGMENT_CONTAINER,
                PlacesFragment.getInstance(placesFragmentListener, dataType))
    }

    private fun openFilter() {
        startActivity(
                CustomToolBarActivity.newIntent(this, CustomToolBarActivity.FILTER)
        )
    }

    private fun openBooking() {
        startActivity(
                FlatToolBarActivity.newIntent(this@CommonActivity, FlatToolBarActivity.BOOKING_TYPE)
        )
    }

    private fun openBaskets() {
        startActivity(PlaceActivity.newIntent(application,
                PlaceActivity.BASKETS_TYPE))
    }

    private fun openDelivery(placeId: Long) {
        startActivity(PlaceActivity.newIntent(application, PlaceActivity.DELIVERY_MENU, placeId))
    }

    private fun openBooking(placeId: Long) {
        startActivity(PlaceActivity.newIntent(application, PlaceActivity.BOOKING, placeId))
    }
    private fun openPlace(placeId: Long) {
        startActivity(PlaceActivity.newIntent(application, PlaceActivity.OPEN_PLACE_TYPE, placeId))
    }

    override fun getViewModelHandler() = object : CommonActivityViewModel.Handler {
    }

    private val placesFragmentListener = object : PlacesFragment.Listener {
        override fun showPlacesMapFragment(dataType: Int) = openPlacesMapFragment(dataType)
        override fun openPlace(placeId: Long) = this@CommonActivity.openPlace(placeId)
        override fun openBooking(placeId: Long) = this@CommonActivity.openBooking(placeId)
        override fun openBooking() = this@CommonActivity.openBooking()
        override fun openDelivery(placeId: Long) = this@CommonActivity.openDelivery(placeId)
        override fun openFilter() = this@CommonActivity.openFilter()
        override fun openBaskets() = this@CommonActivity.openBaskets()
        override fun toolbarTitle(title: String)  = this@CommonActivity.toolbarTitle(title, false)
    }

    private val placesMapFragmentListener = object : PlacesMapFragment.Listener {
        override fun openPlacesFragment(dataType: Int) = this@CommonActivity.openPlacesFragment(dataType)
        override fun openBooking() = this@CommonActivity.openBooking()
        override fun openPlace(placeId: Long) = this@CommonActivity.openPlace(placeId)
        override fun openBaskets() = this@CommonActivity.openBaskets()
        override fun openFilter() = this@CommonActivity.openFilter()
        override fun toolbarTitle(title: String) = this@CommonActivity.toolbarTitle(title, false)
    }

    private val walletFragmentListener = object : WalletFragment.Listener {
        override fun openInfo() {
            replaceFragment(
                    FRAGMENT_CONTAINER,
                    InfoFragment.getInstance(infoFragmentListener),
                    true, "",true
            )
        }
    }


    private val termsAndConditionsFragmentListener = object : TermsAndConditionsFragment.Listener {
        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val advantagesFragmentListener = object : AdvantagesFragment.Listener {
        override fun openLogin() {
            startActivity(LoginActivity.newIntent(this@CommonActivity))
        }

        override fun toolbarTitle(title: String) {
            if (title == "") {
                supportActionBar?.hide()
            } else {
                supportActionBar?.title = title
            }
        }
    }

    private val promotionFragmentListener = object : PromotionFragment.Listener {
        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }

        override fun openComplaintsFragment() {
            this@CommonActivity.openComplaintsFragment()
        }
    }


    private val infoFragmentListener = object : InfoFragment.Listener {
        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val complaintsFragmentListener = object : ComplaintsFragment.Listener {
        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }

        override fun openCategoryList(categoryName: String,
                                      categoryListener: RecyclerViewFragment.CategoryListener) {
            replaceFragment(
                    FRAGMENT_CONTAINER,
                    RecyclerViewFragment.getInstance(
                            recyclerViewFragmentListener, categoryListener, -1, categoryName),
                    true
            )
        }
    }

    private val recyclerViewFragmentListener = object : RecyclerViewFragment.Listener {
        override fun getComplaintText(item: String) = onBackPressed()
        override fun showNavigationDrawerToolbarButton() {// TODO("not implemented")
        }
        override fun showBackToolbarButton() {// TODO("not implemented")
        }
    }
}
