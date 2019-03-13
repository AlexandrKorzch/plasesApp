package net.caffee.places.activity.main

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.Profile

class MainViewModel(context: Application, repository: Repository)
    : BaseViewModel<MainActivity.ViewModelHandler>(context, repository), LifecycleObserver {

    private var isSalePressed = ObservableField<Boolean>()
    private var isSettingsPressed = ObservableField<Boolean>()
    private var isSupportPressed = ObservableField<Boolean>()
    private var isFaqPressed = ObservableField<Boolean>()
    private var isTermsPressed = ObservableField<Boolean>()

    val name = ObservableField<String>("")
    val city = ObservableField<String>("")
    val image = ObservableField<String>("")

    init {
        isSalePressed.set(false)
        isSettingsPressed.set(false)
        isSupportPressed.set(false)
        isFaqPressed.set(false)
        isTermsPressed.set(false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getProfile() {
        addDisposable(repository.getProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetProfile(it) },
                        ApiErrorHandler(this)))
    }

    private fun onGetProfile(profile: Profile) {
        name.set(profile.firstName+" "+profile.lastName)
        image.set(profile.image)
        city.set(profile.cityTitle)
    }

    fun pressCloseNavigationDrawerButton() {
        getHandler().onCloseNavigationDrawerPressed()
    }

    fun pressProfileButton() {
        getHandler().onProfilePressed()
    }

    fun pressEditProfileButton() {
        getHandler().onEditProfilePressed()
    }

    fun pressExitButton() {
        getHandler().onExitPressed()
    }

    fun pressSaleButton() {
        changeState(isSalePressed)
        getHandler().onSalePressed()
    }

    fun pressSettingsButton() {
        changeState(isSettingsPressed)
        getHandler().onSettingsPressed()
    }

    fun pressSupportButton() {
        changeState(isSupportPressed)
        getHandler().onSupportPressed()
    }

    fun pressFaqButton() {
        changeState(isFaqPressed)
        getHandler().onFaqPressed()
    }

    fun pressTermsButton() {
        changeState(isTermsPressed)
        getHandler().onTermsPressed()
    }

    private fun changeState(pressed: ObservableField<Boolean>) {
        resetAllNavigationDrawerButtons()
        pressed.set(true)
    }

    fun resetAllNavigationDrawerButtons() {
        isSalePressed.set(false)
        isSettingsPressed.set(false)
        isSupportPressed.set(false)
        isFaqPressed.set(false)
        isTermsPressed.set(false)
    }

    fun isSalePressed() = isSalePressed

    fun isSettingsPressed() = isSettingsPressed

    fun isSupportPressed() = isSupportPressed

    fun isFaqPressed() = isFaqPressed

    fun isTermsPressed() = isTermsPressed

    fun setSignIn(signIn: Boolean) = repository.setSignIn(signIn)

}