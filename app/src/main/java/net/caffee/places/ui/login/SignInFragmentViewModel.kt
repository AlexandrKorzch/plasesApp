package net.caffee.places.ui.login

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableBoolean
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.SignInRequestBody


class SignInFragmentViewModel(context: Application, repository: Repository)
    : BaseViewModel<SignInFragmentViewModel.Handler>(context, repository),
        LifecycleObserver {

    private val PHONE_LENGTH = 10

    private val checkedObs = ObservableBoolean(false)
    private val backGround = ObservableBoolean(false)

    val checkIsClickable = ObservableBoolean(false)

    var phone: String = ""

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        checkedObs.set(repository.getTermsChecked()!!)
        checkIsClickable.set(!repository.isSignIn())
        checkPhoneAndAgreements()
    }

    fun checked() = checkedObs

    fun backGround() = backGround

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        phone = s.toString()
        checkPhoneAndAgreements()
    }

    fun onCheck(checked: Boolean) {
        repository.setTermsChecked(checked)
        checkedObs.set(checked)
        checkPhoneAndAgreements()
    }

    private fun checkPhoneAndAgreements() = backGround.set(phone.length > PHONE_LENGTH && checkedObs.get())

    fun submitClick() = signIn()

    private fun signIn() {
        showProgress()
        addDisposable(repository.signIn(SignInRequestBody(phone))
                .subscribe(
                        Consumer {
                            hideProgress()
                            getHandler().openConfirmation()
                        },
                        ApiErrorHandler(this),
                        Action{hideProgress()}
                )
        )
    }

    interface Handler : BaseHandler {
        fun openConfirmation()
    }
}