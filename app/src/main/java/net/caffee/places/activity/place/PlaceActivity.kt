package net.caffee.places.activity.place

import android.content.Context
import android.content.Intent
import android.os.Bundle
import net.caffee.places.R
import net.caffee.places.activity.common.CommonActivity
import net.caffee.places.activity.flat.FlatToolBarActivity
import net.caffee.places.activity.fullscreen.FullScreenActivity
import net.caffee.places.architecture.BaseActivity
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.ActivityPlaceBinding
import net.caffee.places.extensions.replaceFragment
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.db.model.basket.Basket
import net.caffee.places.repo.remote.model.ProdSubCategory
import net.caffee.places.ui.carts.CartsFragment
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.ui.complaints.ComplaintsFragment
import net.caffee.places.ui.menu.MenuFragment
import net.caffee.places.ui.menucategories.MenuCategoriesFragment
import net.caffee.places.ui.place.fragment.PlaceFragment
import net.caffee.places.ui.reservation.ReservationFragment
import net.caffee.places.ui.review.ReviewFragment
import net.caffee.places.util.HALL


class PlaceActivity : BaseActivity<ActivityPlaceBinding, PlaceActivityViewModel>() {

    companion object {
        private const val TAG = "PlaceActivity"
        private const val FRAGMENT_CONTAINER = R.id.fragmentContainer
        private const val EXTRA_SCREEN_TYPE = "net.vjet.myplace.activity.place.PlaceActivity.screenType"
        const val BASKETS_TYPE = 1
        const val OPEN_PLACE_TYPE = 2
        const val BOOKING = 3
        const val DELIVERY_MENU = 4

        fun newIntent(context: Context?, screenType: Int = OPEN_PLACE_TYPE, placeId: Long = 0) =
                Intent(context, PlaceActivity::class.java).apply {
                    putExtra(EXTRA_SCREEN_TYPE, screenType)
                    putExtra(PLACE_ID, placeId)
                }
    }

    private var screenType = CommonActivity.TYPE_TERMS_AND_CONDITIONS

    private var placeId = 0L

    override fun layoutResId() = R.layout.activity_place

    override fun viewModelClass() = PlaceActivityViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extra()
        setupUi()
    }

    private fun extra() {
        screenType = intent.getIntExtra(EXTRA_SCREEN_TYPE, OPEN_PLACE_TYPE)
        placeId = intent.getLongExtra(PLACE_ID, placeId)
    }

    private fun setupUi() {
        setupToolbar(binding.toolbarPl, true, R.drawable.ic_arrow_back_black)
        when (screenType) {
            OPEN_PLACE_TYPE -> openPlaceFragment(placeId)
            BASKETS_TYPE -> openAllBaskets(false)
            BOOKING -> openReservationFragment(false)
            DELIVERY_MENU -> openDeliveryMenu(false)
        }
    }

    private fun openDeliveryMenu(backStack: Boolean) {
        replaceFragment(
                FRAGMENT_CONTAINER,
                MenuFragment.getInstance(menuFragmentListener, placeId),
                backStack, "", backStack
        )
    }

    private fun openReservationFragment(backStack: Boolean) {
        replaceFragment(
                FRAGMENT_CONTAINER,
                ReservationFragment.getInstance(reservationFragmentListener, placeId),
                backStack, "", backStack
        )
    }

    private fun openBooking() {
        startActivity(
                FlatToolBarActivity.newIntent(this@PlaceActivity, FlatToolBarActivity.BOOKING_TYPE)
        )
    }

    private fun openBaskets() {
        openAllBaskets(true)
    }

    private fun openAllBaskets(backStack: Boolean) {
        replaceFragment(FRAGMENT_CONTAINER,
                CartsFragment.getInstance(cartsFragmentListener),
                backStack, "", backStack
        )
    }

    private fun openPlaceFragment(placeId: Long) {
        replaceFragment(FRAGMENT_CONTAINER,
                PlaceFragment.getInstance(placeFragmentListener, placeId)
        )
    }

    private fun openNewReviewPage(placeId: Long) {
        replaceFragment(
                FRAGMENT_CONTAINER,
                ReviewFragment.getInstance(reviewFragmentListener, placeId),
                true, "", true
        )
    }

    private fun openOneBasket(basketId: Long?) {
        startActivity(FullScreenActivity
                .newIntent(this@PlaceActivity, FullScreenActivity.ONE_BASKET_TYPE).apply {
                    putExtra(PLACE_ID, basketId)
                })
    }

    override fun getViewModelHandler() = object : PlaceActivityViewModel.Handler {
    }

    private val placeFragmentListener = object : PlaceFragment.Listener {
        override fun openDelivery() = openDeliveryMenu(true)
        override fun openBaskets(baskets: Int) = this@PlaceActivity.openBaskets()
        override fun openBooking() = this@PlaceActivity.openBooking()
        override fun openReservationFragment() = this@PlaceActivity.openReservationFragment(true)
        override fun openMenuFragment() = this@PlaceActivity.openDeliveryMenu(true)
        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }

        override fun openOneBasketFragment(placeId: Long) = this@PlaceActivity.openOneBasket(placeId)

        override fun openComplaintsFragment() {
            replaceFragment(
                    FRAGMENT_CONTAINER,
                    ComplaintsFragment.getInstance(complaintsFragmentListener, placeId),
                    true, "", true
            )
        }

        override fun openNewReviewPage(placeId: Long) = this@PlaceActivity.openNewReviewPage(placeId)
    }

    private val cartsFragmentListener = object : CartsFragment.Listener {

        override fun openOneBasket(item: Basket) = this@PlaceActivity.openOneBasket(item.id)

        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val reservationFragmentListener = object : ReservationFragment.Listener {

        override fun openHallList(placeId: Long, categoryListener: RecyclerViewFragment.CategoryListener) {
            replaceFragment(FRAGMENT_CONTAINER,
                    RecyclerViewFragment.getInstance(
                            recyclerViewFragmentListener, categoryListener, placeId, HALL),
                    true, "", true
            )
        }

        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val menuFragmentListener = object : MenuFragment.Listener {
        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }

        override fun openMenuCategories(subCategory: ProdSubCategory) {
            replaceFragment(
                    FRAGMENT_CONTAINER,
                    MenuCategoriesFragment.getInstance(menuCategoriesFragmentListener, subCategory.id,
                            subCategory.placeId, subCategory.title),
                    true, "", true
            )
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
                    true, "", true
            )
        }
    }

    private val reviewFragmentListener = object : ReviewFragment.Listener {
        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }

    private val recyclerViewFragmentListener = object : RecyclerViewFragment.Listener {
        override fun getComplaintText(item: String) {
            onBackPressed()
        }

        override fun showNavigationDrawerToolbarButton() {
            // TODO("not implemented")
        }

        override fun showBackToolbarButton() {
            // TODO("not implemented")
        }
    }

    private val menuCategoriesFragmentListener = object : MenuCategoriesFragment.Listener {
        override fun openBaskets() = this@PlaceActivity.openBaskets()
        override fun openBooking() = this@PlaceActivity.openBooking()
        override fun toolbarTitle(title: String) {
            supportActionBar?.title = title
        }
    }


    interface ViewModelHandler : BaseHandler
}
