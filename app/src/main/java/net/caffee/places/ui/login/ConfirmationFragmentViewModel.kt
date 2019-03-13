package net.caffee.places.ui.login

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.BaseDto
import net.caffee.places.repo.remote.model.SignIn
import net.caffee.places.repo.remote.model.SignInRequestBody

class ConfirmationFragmentViewModel(context: Application, repository: Repository)
    : BaseViewModel<ConfirmationFragmentViewModel.Handler>(context, repository),
        LifecycleObserver {

    val phone = ObservableField<String>("")
    val code = ObservableField<String>("")
    val backGround = ObservableBoolean(false)

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        backGround.set(s.length >= 4)
    }

    fun sendMoreClick() {
        backGround.set(false)
        code.set("")
        submitRequest(null, {})
    }

    fun submitClick() {
        submitRequest(code.get(), { getHandler().openMain() })
    }

    private fun submitRequest(code: String? = null, function: () -> Unit) {
        showProgress()
        val body = SignInRequestBody(phone.get()!!)
        code?.let { body.code = code }
        addDisposable(repository.signIn(body)
                .subscribe(
                        Consumer {
                            saveAuthKey(it)
                            setSignIn()
                            setFirstEnter()
                            hideProgress()
                            function()
                        },
                        ApiErrorHandler(this),
                        Action { hideProgress() }
                )
        )
    }

    private fun setSignIn() {
        repository.setSignIn(true)
    }

    private fun setFirstEnter() {
        repository.setFirstEnter(false)
    }

    private fun saveAuthKey(it: BaseDto<List<SignIn>>) {
        val signInModel = it.data?.first()
        signInModel?.let { repository.setAuthKey(signInModel.authKey) }
    }

    interface Handler : BaseHandler {
        fun openMain()
    }
}