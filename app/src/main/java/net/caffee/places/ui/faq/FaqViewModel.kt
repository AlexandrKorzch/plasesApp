package net.caffee.places.ui.faq

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableArrayList
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.Contact
import net.caffee.places.repo.remote.model.Fag
import net.caffee.places.repo.remote.model.Fags


class FaqViewModel (context: Application, repository: Repository)
    : BaseViewModel<FaqViewModel.Handler>(context, repository),
        LifecycleObserver {

    val items = ObservableArrayList<Fag>()

    init {
        showProgress()
        addDisposable(repository.getFags()
                .subscribe(Consumer { onGetFags(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetFags(fags: Fags) {
        items.clear()
        items.addAll(fags.fags)
        getHandler().setContact(fags.contact)
    }

    interface Handler : BaseHandler{
        fun setContact(contact: Contact?)
    }
}