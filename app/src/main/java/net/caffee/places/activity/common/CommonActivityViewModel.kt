package net.caffee.places.activity.common

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class CommonActivityViewModel (context: Application, repository: Repository)
    : BaseViewModel<CommonActivityViewModel.Handler>(context, repository), LifecycleObserver {

    interface Handler : BaseHandler
}