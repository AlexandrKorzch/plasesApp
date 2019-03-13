package net.caffee.places.activity.splash

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.provider.Settings
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.util.AskPermission

class SplashViewModel(context: Application, repository: Repository)
    : BaseViewModel<SplashViewModel.Handler>(context, repository),
        LifecycleObserver {

    fun decidePage() {
        with(getHandler()) {
            when {
                repository.isSignIn() -> openMain()
                repository.isSFirstEnter() -> {
                    getDeviceId()
                    advantagesRequest()
                }
                else -> openLogin()
            }
        }
    }

    private fun advantagesRequest() {
        askPermission(AskPermission({
            showProgress()
            addDisposable(repository.getAdvantages(it)
                    .subscribe(Consumer { },
                            ApiErrorHandler(this),
                            Action { onComplete() }))
        }, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)))
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId() {
        repository.setDeviceId(Settings.Secure.getString(
                getApplication<Application>().contentResolver, Settings.Secure.ANDROID_ID))
    }

    private fun onComplete() {
        hideProgress()
        getHandler().openAdvantages()
    }

    interface Handler : BaseHandler {
        fun openAdvantages()
        fun openLogin()
        fun openMain()
    }
}