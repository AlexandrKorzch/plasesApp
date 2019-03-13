package net.caffee.places.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.GravityCompat
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.caffee.places.R
import net.caffee.places.activity.common.CommonActivity
import net.caffee.places.activity.flat.FlatToolBarActivity
import net.caffee.places.activity.fullscreen.FullScreenActivity
import net.caffee.places.activity.login.LoginActivity
import net.caffee.places.activity.menutitle.CustomToolBarActivity
import net.caffee.places.activity.place.PlaceActivity
import net.caffee.places.architecture.BaseActivity
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.ActivityMainBinding
import net.caffee.places.extensions.*
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.ui.faq.FaqFragment
import net.caffee.places.ui.invoice.InvoiceFragment
import net.caffee.places.ui.invoice.bill.BillFragment
import net.caffee.places.ui.invoice.paid.BillIsPaidFragment
import net.caffee.places.ui.minifilter.MiniFilterDialog
import net.caffee.places.ui.notifications.NotificationsFragment
import net.caffee.places.ui.places.PlacesFragment
import net.caffee.places.ui.places.PlacesFragment.Companion.ALL_PLACES
import net.caffee.places.ui.places.PlacesFragment.Companion.FAVORITE_PLACES
import net.caffee.places.ui.places.PlacesFragment.Companion.PLACES_WITH_DELIVERY
import net.caffee.places.ui.places.PlacesMapFragment
import net.caffee.places.ui.promotions.PromotionsFragment
import net.caffee.places.ui.settings.SettingsFragment
import net.caffee.places.ui.support.SupportFragment
import net.caffee.places.util.LANGUAGE
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    companion object {
        private const val TAG = "MainActivity"
        private const val FRAGMENT_CONTAINER = R.id.fragmentContainer

        fun newIntent(context: Context?) = Intent(context, MainActivity::class.java)
    }

    private lateinit var placesFragment: PlacesFragment

    override fun layoutResId() = R.layout.activity_main

    override fun viewModelClass() = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setupToolbar()
        openAllPlacesFragment()
    }

    private fun setupToolbar() {
        setupToolbar(toolbar, true, R.drawable.ic_arrow_back_black)
        setupBottomNavigationDrawer()
        showNavigationDrawerToolbarButton()
    }

    private fun showNavigationDrawerToolbarButton() {
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun showBackToolbarButton() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupBottomNavigationDrawer() {
        bottomNavigationView.removeShiftingMode()
        bottomNavigationView.selectedItemId = R.id.placesItem
        bottomNavigationView.setOnNavigationItemSelectedListener(
                onNavigationItemSelectedListener)
    }

    private fun showMiniFilterBottomSheetDialogFragment() {
        MiniFilterDialog.getInstance(object : MiniFilterDialog.Listener {
            override fun requestPlaces() = placesFragment.request()
        }).show(supportFragmentManager, MiniFilterDialog.TAG)
    }

    private fun toolbarTitle(title: String, appBarOutline : Boolean){
        supportActionBar?.title = title
        if(appBarOutline) showAppBarOutline()
        else removeAppBarOutline()
    }

    private fun openAllPlacesFragment() {
        if (isPlacesFragmentOpened()) {
            showMiniFilterBottomSheetDialogFragment()
        } else {
            openPlacesFragment(ALL_PLACES)
        }
    }

    private fun openPlacesFragment(dataType: Int) {
        placesFragment = PlacesFragment.getInstance(placesFragmentListener, dataType)
        replaceFragmentFromBottom(FRAGMENT_CONTAINER, placesFragment, "$dataType")
    }

    private fun openPlacesMapFragment(dataType: Int) {
        replaceFragmentFromBottom(FRAGMENT_CONTAINER,
                PlacesMapFragment.getInstance(placesMapFragmentListener, dataType))
    }

    private fun openNotificationsFragment() {
        replaceFragmentFromBottom(FRAGMENT_CONTAINER,
                NotificationsFragment.getInstance(notificationsFragmentListener))
    }

    private fun showInvoicePage() {
        replaceFragmentFromBottom(FRAGMENT_CONTAINER,
                InvoiceFragment.getInstance(invoiceFragmentListener))
    }

    private fun openPromotion(promotionId: Long) {
        startActivity(
                CommonActivity.newIntent(this, CommonActivity.TYPE_PROMOTION).apply {
                    putExtra(CommonActivity.PROMOTION_ID, promotionId)
                }
        )
    }

    private fun openFilter() {
        startActivity(CustomToolBarActivity.newIntent(this, CustomToolBarActivity.FILTER))
    }

    private fun openBooking() {
        startActivity(FlatToolBarActivity.newIntent(this@MainActivity, FlatToolBarActivity.BOOKING_TYPE))
    }

    private fun openBaskets() {
        startActivity(PlaceActivity.newIntent(application,
                PlaceActivity.BASKETS_TYPE))
    }

    private fun openLanguagesList() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                RecyclerViewFragment.getInstance(
                        null,
                        object : RecyclerViewFragment.CategoryListener {
                            override fun changeCategory(item: BaseCategory) {
                                SettingsFragment.languageStr = item.title //todo remove this logic
                                onBackPressed()
                            }
                        }, -1, LANGUAGE),
                true, "", true
        )
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

    private fun isPlacesFragmentOpened(): Boolean {
        val openedFragment
                = supportFragmentManager.findFragmentByTag(
                PlacesFragment::class.simpleName+ALL_PLACES)
        return openedFragment != null
    }

    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
                resetDrawerButtons()
                enableBottomButtons()
                menuItem.isChecked = true
                when (menuItem.itemId) {
                    R.id.favoriteItem -> openPlacesFragment(FAVORITE_PLACES)
                    R.id.notificationsItem -> openNotificationsFragment()
                    R.id.placesItem -> openAllPlacesFragment()
                    R.id.invoiceItem -> showInvoicePage()
                    R.id.deliveryItem -> openPlacesFragment(PLACES_WITH_DELIVERY)
                }
                true
            }

    private val placesFragmentListener = object : PlacesFragment.Listener {
        override fun showPlacesMapFragment(dataType: Int) = openPlacesMapFragment(dataType)
        override fun openPlace(placeId: Long) = this@MainActivity.openPlace(placeId)
        override fun openBooking(placeId: Long) = this@MainActivity.openBooking(placeId)
        override fun openBooking() = this@MainActivity.openBooking()
        override fun openDelivery(placeId: Long) = this@MainActivity.openDelivery(placeId)
        override fun openFilter() = this@MainActivity.openFilter()
        override fun openBaskets() = this@MainActivity.openBaskets()
        override fun toolbarTitle(title: String)  = this@MainActivity.toolbarTitle(title, false)
    }

    private val placesMapFragmentListener = object : PlacesMapFragment.Listener {
        override fun openPlacesFragment(dataType: Int) = this@MainActivity.openPlacesFragment(dataType)
        override fun openBooking() = this@MainActivity.openBooking()
        override fun openPlace(placeId: Long) = this@MainActivity.openPlace(placeId)
        override fun openBaskets() = this@MainActivity.openBaskets()
        override fun openFilter() = this@MainActivity.openFilter()
        override fun toolbarTitle(title: String) = this@MainActivity.toolbarTitle(title, false)
    }

    private val notificationsFragmentListener = object : NotificationsFragment.Listener {
        override fun toolbarTitle(title: String) = this@MainActivity.toolbarTitle(title, true)
        override fun openBooking(placeId: Long) = this@MainActivity.openBooking(placeId)
        override fun openPlacesNearYou() =
            startActivity(CommonActivity
                    .newIntent(this@MainActivity, CommonActivity.TYPE_PLACES_NEAR_YOU))
    }

    private val invoiceFragmentListener = object : InvoiceFragment.Listener {
        override fun toolbarTitle(title: String) = this@MainActivity.toolbarTitle(title, true)
        override fun onGetRequestForOrder() {
            toast("Ожидайте счет 1 секунду") //todo change logic
            Handler().postDelayed({
                replaceFragmentFromBottom(FRAGMENT_CONTAINER,
                        BillFragment.getInstance(billFragmentListener))
            }, TimeUnit.SECONDS.toMillis(1))
        }
    }

    private val faqFragmentListener = object : FaqFragment.Listener {
        override fun toolbarTitle(title: String) = this@MainActivity.toolbarTitle(title, true)
    }

    private val promotionsFragmentListener = object : PromotionsFragment.Listener {
        override fun toolbarTitle(title: String) = this@MainActivity.toolbarTitle(title, true)
        override fun openPromotion(promotionId: Long) = this@MainActivity.openPromotion(promotionId)
    }

    private val supportFragmentListener = object : SupportFragment.Listener {
        override fun toolbarTitle(title: String) = this@MainActivity.toolbarTitle(title, true)
        override fun openCategoryList(categoryName: String,
                                      categoryListener: RecyclerViewFragment.CategoryListener) {
            replaceFragment(FRAGMENT_CONTAINER,
                    RecyclerViewFragment.getInstance(
                            recyclerViewFragmentListener, categoryListener, -1, categoryName),
                    true, "", true
            )
        }
    }

    private val settingsFragmentListener = object : SettingsFragment.Listener {
        override fun toolbarTitle(title: String) = this@MainActivity.toolbarTitle(title, true)
        override fun changeLanguage() = openLanguagesList()
    }

    private val billFragmentListener = object : BillFragment.Listener {
        override fun toolbarTitle(title: String) = this@MainActivity.toolbarTitle(title, true)
        override fun openPayment() { //todo add payment system
            toast("Open Payment Page")
            Handler().postDelayed({ toast("After payment") }, 1000)
            Handler().postDelayed({
                replaceFragmentFromBottom(FRAGMENT_CONTAINER,
                        BillIsPaidFragment.getInstance(billPaidFragmentListener))
            }, 2000)
        }
    }

    private val billPaidFragmentListener = object : BillIsPaidFragment.Listener {
        override fun closeBillPaidPage() = showInvoicePage()
    }

    private val recyclerViewFragmentListener = object : RecyclerViewFragment.Listener {
        override fun getComplaintText(item: String) = onBackPressed()
        override fun showBackToolbarButton() = this@MainActivity.showBackToolbarButton()
        override fun showNavigationDrawerToolbarButton() {
            this@MainActivity.showNavigationDrawerToolbarButton()
            popBackStack()
        }
    }

    override fun getViewModelHandler() = object : ViewModelHandler {
        override fun onCloseNavigationDrawerPressed() = closeDrawer()

        override fun onProfilePressed() {
            resetDrawerButtons()
            startActivity(FullScreenActivity.newIntent(application, FullScreenActivity.EDIT_PROFILE_TYPE))
        }

        override fun onEditProfilePressed() {
            resetDrawerButtons()
            startActivity(CustomToolBarActivity.newIntent(application, CustomToolBarActivity.EDIT_PROFILE))
        }

        override fun onExitPressed() {
            viewModel().setSignIn(false)
            startActivity(LoginActivity.newIntent(applicationContext).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
        }

        override fun onSalePressed() {
            closeDrawerWithResetBottomButtons()
            replaceFragment(FRAGMENT_CONTAINER,
                    PromotionsFragment.getInstance(promotionsFragmentListener), false, "",true)
        }

        override fun onSettingsPressed() {
            closeDrawerWithResetBottomButtons()
            replaceFragment(FRAGMENT_CONTAINER,
                    SettingsFragment.getInstance(settingsFragmentListener), false, "",true)
        }

        override fun onSupportPressed() {
            closeDrawerWithResetBottomButtons()
            replaceFragment(FRAGMENT_CONTAINER,
                    SupportFragment.getInstance(supportFragmentListener), false, "",true)
        }

        override fun onFaqPressed() {
            closeDrawerWithResetBottomButtons()
            replaceFragment(FRAGMENT_CONTAINER,
                    FaqFragment.getInstance(faqFragmentListener), false, "",true)
        }

        override fun onTermsPressed() {
            startActivity(CommonActivity.newIntent(
                    application, CommonActivity.TYPE_TERMS_AND_CONDITIONS))
        }
    }

    private fun closeDrawer() = binding.drawerLayout.closeDrawers()
    private fun enableBottomButtons() = bottomNavigationView.menu.setGroupCheckable(0, true, true)
    private fun resetDrawerButtons() = viewModel().resetAllNavigationDrawerButtons()
    private fun closeDrawerWithResetBottomButtons(){
        popBackStack()
        closeDrawer()
        bottomNavigationView.menu.setGroupCheckable(0, false, true)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    interface ViewModelHandler : BaseHandler {
        fun onCloseNavigationDrawerPressed()
        fun onProfilePressed()
        fun onEditProfilePressed()
        fun onExitPressed()
        fun onSalePressed()
        fun onSettingsPressed()
        fun onSupportPressed()
        fun onFaqPressed()
        fun onTermsPressed()
    }
}
