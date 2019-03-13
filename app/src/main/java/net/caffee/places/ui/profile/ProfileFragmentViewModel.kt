package net.caffee.places.ui.profile

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableField
import android.databinding.ObservableInt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.Profile

class ProfileFragmentViewModel(context: Application, repository: Repository)
    : BaseViewModel<ProfileFragmentViewModel.Handler>(context, repository),
        LifecycleObserver {


    val name = ObservableField<String>("")
    val city = ObservableField<String>("")
    val image = ObservableField<String>("")
    private var balance = ObservableInt(0)
    private var symbol = ObservableField<String>("₸")
    private var booking = ObservableInt(2)

    fun symbol() = symbol
    fun balance() = balance
    fun booking() = booking

    fun walletClick() = getHandler().openWallet()
    fun bookingClick() = getHandler().openBooking()
    fun paymentClick()  = getHandler().openPayments()
    fun editProfileClick() = getHandler().editProfile()

    fun balanceClick() {
        getHandler().openPayment()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getProfile() {
        showProgress()
        addDisposable(repository.getProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetProfile(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetProfile(profile: Profile) {
        balance.set(profile.walletBalance)
        name.set(profile.firstName+" "+profile.lastName)
        image.set(profile.image)
        city.set(profile.cityTitle)
    }

    interface Handler : BaseHandler {
        fun editProfile()
        fun openWallet()
        fun openPayments()
        fun openBooking()
        fun openPayment()
    }
}