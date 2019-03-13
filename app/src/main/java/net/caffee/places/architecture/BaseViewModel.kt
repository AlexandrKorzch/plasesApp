package net.caffee.places.architecture

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import net.caffee.places.repo.Repository
import net.caffee.places.util.AskPermission
import net.caffee.places.util.SingleLiveEvent
import net.caffee.places.util.logWithTAG


abstract class BaseViewModel<out H : BaseHandler>(context: Application, val repository
: Repository) : AndroidViewModel(context), LifecycleObserver {

    val dataLoadingEvent = SingleLiveEvent<Boolean>()
    val showErrorEvent = SingleLiveEvent<String>()
    val askPermissionEvent = SingleLiveEvent<AskPermission>()

    val disposables = CompositeDisposable()

    private lateinit var handler: H

    fun setHandler(h: BaseHandler) {
        handler = h as H
    }

    protected fun getHandler(): H = handler

    fun addDisposable(disposable: Disposable){
        disposables.add(disposable)
    }

    fun showProgress() = dataLoadingEvent.postValue(true)
    fun hideProgress() = dataLoadingEvent.postValue(false)

    fun showError(message: String?){
        showErrorEvent.postValue(message)
    }

    fun askPermission(askPermission: AskPermission) {
        askPermissionEvent.postValue(askPermission)
    }

    override fun onCleared() {
        super.onCleared()
        logWithTAG("BaseViewModel ${this::class.java.simpleName} onCleared()")
        disposables.dispose()
        disposables.clear()
    }
}