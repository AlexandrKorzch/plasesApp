package net.caffee.places.activity.menutitle

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.activity.login.LoginActivityViewModel
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class CustomToolBarViewModel(context: Application, repository: Repository)
    : BaseViewModel<LoginActivityViewModel.Handler>(context, repository), LifecycleObserver {


    interface Handler : BaseHandler {
    }
}