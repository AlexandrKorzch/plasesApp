package net.caffee.places.ui.support.dialog

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class SupportDialogViewModel(context: Application, repository: Repository)
    : BaseViewModel<SupportDialogViewModel.Handler>(context, repository),
        LifecycleObserver {

    fun closeDialog() {
        getHandler().closeDialog()
    }

    interface Handler:BaseHandler{
        fun closeDialog()
    }

}