package net.caffee.places.ui.termsandconditions

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler


class TermsAndConditionsViewModel(context: Application, repository: Repository)
    : BaseViewModel<TermsAndConditionsViewModel.Handler>(context, repository), LifecycleObserver {

    val isChecked = ObservableField<Boolean>()
    val checkIsClickable = ObservableBoolean(false)

    init {
        showProgress()
        addDisposable(repository.getPages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetUrl(it) },
                        ApiErrorHandler(this))
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        isChecked.set(repository.getTermsChecked())
        checkIsClickable.set(!repository.isSignIn())
    }

    private fun onGetUrl(url: String) {
        getHandler().showTermsAndConditiions(url)
    }

    fun onCheck(checked: Boolean){
        repository.setTermsChecked(checked)
    }

    interface Handler : BaseHandler{
        fun showTermsAndConditiions(url: String)
    }
}
