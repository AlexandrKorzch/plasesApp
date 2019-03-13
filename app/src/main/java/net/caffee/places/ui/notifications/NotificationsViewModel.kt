package net.caffee.places.ui.notifications

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class NotificationsViewModel(context: Application, repository: Repository)
    : BaseViewModel<NotificationsViewModel.Handler>(context, repository),
        LifecycleObserver {

    companion object {
        const val NEW_NOTIFICATION_ITEM_OFFSET = 8
    }

    fun list() = mutableListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    fun offsetPosition() = 1

    interface Handler : BaseHandler
}