package net.caffee.places.ui.booking

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class BookingViewModel(context: Application, repository: Repository)
    : BaseViewModel<BookingViewModel.Handler>(context, repository),
        LifecycleObserver {

    interface Handler : BaseHandler{}
}