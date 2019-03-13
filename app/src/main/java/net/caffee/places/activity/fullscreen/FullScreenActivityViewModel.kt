package net.caffee.places.activity.fullscreen

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class FullScreenActivityViewModel (context: Application, repository: Repository)
    : BaseViewModel<FullScreenActivityViewModel.Handler>(context, repository), LifecycleObserver {

    interface Handler : BaseHandler
}