package net.caffee.places.architecture

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import net.caffee.places.activity.common.CommonActivityViewModel
import net.caffee.places.activity.flat.FlatToolBarViewModel
import net.caffee.places.activity.fullscreen.FullScreenActivityViewModel
import net.caffee.places.activity.login.LoginActivityViewModel
import net.caffee.places.activity.main.MainViewModel
import net.caffee.places.activity.menutitle.CustomToolBarViewModel
import net.caffee.places.activity.place.PlaceActivityViewModel
import net.caffee.places.activity.splash.SplashViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.ui.advantages.AdvantagesViewModel
import net.caffee.places.ui.booking.BookingCurrentViewModel
import net.caffee.places.ui.booking.BookingPastViewModel
import net.caffee.places.ui.booking.BookingViewModel
import net.caffee.places.ui.cart.CartViewModel
import net.caffee.places.ui.carts.CartsViewModel
import net.caffee.places.ui.common.fragments.RecyclerViewViewModel
import net.caffee.places.ui.complaints.ComplaintDialogViewModel
import net.caffee.places.ui.complaints.ComplaintsViewModel
import net.caffee.places.ui.faq.FaqViewModel
import net.caffee.places.ui.filter.FilterViewModel
import net.caffee.places.ui.invoice.InvoiceViewModel
import net.caffee.places.ui.invoice.bill.BillViewModel
import net.caffee.places.ui.login.ConfirmationFragmentViewModel
import net.caffee.places.ui.login.SignInFragmentViewModel
import net.caffee.places.ui.menu.MenuViewModel
import net.caffee.places.ui.menucategories.MenuCategoriesViewModel
import net.caffee.places.ui.menuitem.MenuItemViewModel
import net.caffee.places.ui.minifilter.MiniFilterViewModel
import net.caffee.places.ui.notification.BookingNotificationViewModel
import net.caffee.places.ui.notifications.NotificationsViewModel
import net.caffee.places.ui.order.OrderBasketViewModel
import net.caffee.places.ui.order.OrderFirstStepViewModel
import net.caffee.places.ui.order.OrderSecondStepViewModel
import net.caffee.places.ui.order.OrderThirdStepViewModel
import net.caffee.places.ui.payments.PaymentsViewModel
import net.caffee.places.ui.place.fragment.PlaceFragmentViewModel
import net.caffee.places.ui.places.PlacesViewModel
import net.caffee.places.ui.profile.EditProfileFragmentViewModel
import net.caffee.places.ui.profile.ProfileFragmentViewModel
import net.caffee.places.ui.promotion.PromotionViewModel
import net.caffee.places.ui.promotions.PromotionsViewModel
import net.caffee.places.ui.reservation.ReservationViewModel
import net.caffee.places.ui.reservation.dialogs.ReservationDialogViewModel
import net.caffee.places.ui.review.ReviewDialogViewModel
import net.caffee.places.ui.review.ReviewViewModel
import net.caffee.places.ui.settings.SettingsViewModel
import net.caffee.places.ui.support.SupportViewModel
import net.caffee.places.ui.support.dialog.SupportDialogViewModel
import net.caffee.places.ui.termsandconditions.TermsAndConditionsViewModel
import net.caffee.places.ui.wallet.WalletViewModel
import net.caffee.places.ui.wallet.info.InfoViewModel


class ViewModelFactory private constructor(
        private val application: Application,
        private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(LoginActivityViewModel::class.java) ->
                        LoginActivityViewModel(application, repository)
                    isAssignableFrom(SignInFragmentViewModel::class.java) ->
                        SignInFragmentViewModel(application, repository)
                    isAssignableFrom(SettingsViewModel::class.java) ->
                        SettingsViewModel(application, repository)
                    isAssignableFrom(MiniFilterViewModel::class.java) ->
                        MiniFilterViewModel(application, repository)
                    isAssignableFrom(InfoViewModel::class.java) ->
                        InfoViewModel(application, repository)
                    isAssignableFrom(ConfirmationFragmentViewModel::class.java) ->
                        ConfirmationFragmentViewModel(application, repository)
                    isAssignableFrom(CustomToolBarViewModel::class.java) ->
                        CustomToolBarViewModel(application, repository)
                    isAssignableFrom(InvoiceViewModel::class.java) ->
                        InvoiceViewModel(application, repository)
                    isAssignableFrom(BillViewModel::class.java) ->
                        BillViewModel(application, repository)
                    isAssignableFrom(BookingNotificationViewModel::class.java) ->
                        BookingNotificationViewModel(application, repository)
                    isAssignableFrom(SplashViewModel::class.java) ->
                        SplashViewModel(application, repository)
                    isAssignableFrom(AdvantagesViewModel::class.java) ->
                        AdvantagesViewModel(application, repository)
                    isAssignableFrom(MainViewModel::class.java) ->
                        MainViewModel(application, repository)
                    isAssignableFrom(PlacesViewModel::class.java) ->
                        PlacesViewModel(application, repository)
                    isAssignableFrom(MenuCategoriesViewModel::class.java) ->
                        MenuCategoriesViewModel(application, repository)
                    isAssignableFrom(PlaceFragmentViewModel::class.java) ->
                        PlaceFragmentViewModel(application, repository)
                    isAssignableFrom(PlaceActivityViewModel::class.java) ->
                        PlaceActivityViewModel(application, repository)
                    isAssignableFrom(ReservationViewModel::class.java) ->
                        ReservationViewModel(application, repository)
                    isAssignableFrom(ReviewViewModel::class.java) ->
                        ReviewViewModel(application, repository)
                    isAssignableFrom(RecyclerViewViewModel::class.java) ->
                        RecyclerViewViewModel(application, repository)
                    isAssignableFrom(FaqViewModel::class.java) ->
                        FaqViewModel(application, repository)
                    isAssignableFrom(ComplaintsViewModel::class.java) ->
                        ComplaintsViewModel(application, repository)
                    isAssignableFrom(TermsAndConditionsViewModel::class.java) ->
                        TermsAndConditionsViewModel(application, repository)
                    isAssignableFrom(CommonActivityViewModel::class.java) ->
                        CommonActivityViewModel(application, repository)
                    isAssignableFrom(FlatToolBarViewModel::class.java) ->
                        FlatToolBarViewModel(application, repository)
                    isAssignableFrom(FullScreenActivityViewModel::class.java) ->
                        FullScreenActivityViewModel(application, repository)
                    isAssignableFrom(BookingPastViewModel::class.java) ->
                        BookingPastViewModel(application, repository)
                    isAssignableFrom(BookingViewModel::class.java) ->
                        BookingViewModel(application, repository)
                    isAssignableFrom(CartViewModel::class.java) ->
                        CartViewModel(application, repository)
                    isAssignableFrom(EditProfileFragmentViewModel::class.java) ->
                        EditProfileFragmentViewModel(application, repository)
                    isAssignableFrom(MenuViewModel::class.java) ->
                        MenuViewModel(application, repository)
                    isAssignableFrom(NotificationsViewModel::class.java) ->
                        NotificationsViewModel(application, repository)
                    isAssignableFrom(OrderFirstStepViewModel::class.java) ->
                        OrderFirstStepViewModel(application, repository)
                    isAssignableFrom(CartsViewModel::class.java) ->
                        CartsViewModel(application, repository)
                    isAssignableFrom(FilterViewModel::class.java) ->
                        FilterViewModel(application, repository)
                    isAssignableFrom(OrderSecondStepViewModel::class.java) ->
                        OrderSecondStepViewModel(application, repository)
                    isAssignableFrom(OrderThirdStepViewModel::class.java) ->
                        OrderThirdStepViewModel(application, repository)
                    isAssignableFrom(PaymentsViewModel::class.java) ->
                        PaymentsViewModel(application, repository)
                    isAssignableFrom(ProfileFragmentViewModel::class.java) ->
                        ProfileFragmentViewModel(application, repository)
                    isAssignableFrom(PromotionViewModel::class.java) ->
                        PromotionViewModel(application, repository)
                    isAssignableFrom(PromotionsViewModel::class.java) ->
                        PromotionsViewModel(application, repository)
                    isAssignableFrom(SupportViewModel::class.java) ->
                        SupportViewModel(application, repository)
                    isAssignableFrom(WalletViewModel::class.java) ->
                        WalletViewModel(application, repository)
                    isAssignableFrom(ComplaintDialogViewModel::class.java) ->
                        ComplaintDialogViewModel(application, repository)
                    isAssignableFrom(MenuItemViewModel::class.java) ->
                        MenuItemViewModel(application, repository)
                    isAssignableFrom(OrderBasketViewModel::class.java) ->
                        OrderBasketViewModel(application, repository)
                    isAssignableFrom(ReservationDialogViewModel::class.java) ->
                        ReservationDialogViewModel(application, repository)
                    isAssignableFrom(ReviewDialogViewModel::class.java) ->
                        ReviewDialogViewModel(application, repository)
                    isAssignableFrom(SupportDialogViewModel::class.java) ->
                        SupportDialogViewModel(application, repository)
                    isAssignableFrom(BookingCurrentViewModel::class.java) ->
                        BookingCurrentViewModel(application, repository)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application,
                            Injection.provideRepository(application.applicationContext))
                            .also { INSTANCE = it }
                }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}