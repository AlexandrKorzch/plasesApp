package net.caffee.places.ui.complaints

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class ComplaintDialogViewModel(context: Application, repository: Repository)
    : BaseViewModel<ComplaintDialogViewModel.Handler>(context, repository),
        LifecycleObserver {

    fun closeDialog() {
        getHandler().closeDialog()
    }

    interface Handler : BaseHandler {
        fun closeDialog()
    }

}