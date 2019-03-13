package net.caffee.places.ui.reservation.dialogs

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class ReservationDialogViewModel(context: Application, repository: Repository)
    : BaseViewModel<ReservationDialogViewModel.Handler>(context, repository),
        LifecycleObserver {

    companion object {
        const val EVENT_ON_CLOSE_DIALOG = 0
    }

    private val events = MutableLiveData<Int>()

    fun events() = events

    fun closeDialog() {
        events.value = EVENT_ON_CLOSE_DIALOG
    }

    interface Handler:BaseHandler{}
}