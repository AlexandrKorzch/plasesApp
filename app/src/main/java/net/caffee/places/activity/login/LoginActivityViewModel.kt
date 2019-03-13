package net.caffee.places.activity.login

import android.Manifest
import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository
import net.caffee.places.util.AskPermission

class LoginActivityViewModel(context: Application, repository: Repository)
    : BaseViewModel<LoginActivityViewModel.Handler>(context, repository), LifecycleObserver {

    init {
        askPermission(AskPermission({getHandler().setSmsPermission(it)}
                , arrayOf(Manifest.permission.RECEIVE_SMS)))
    }

    interface Handler : BaseHandler {
        fun setSmsPermission(allow: Boolean)
    }
}