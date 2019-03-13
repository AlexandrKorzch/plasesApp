package net.caffee.places.activity.place

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository

class PlaceActivityViewModel(context: Application, repository: Repository)
    : BaseViewModel<PlaceActivityViewModel.Handler>(context, repository), LifecycleObserver {

    fun text() = "Example"


    interface Handler : BaseHandler {

    }

}