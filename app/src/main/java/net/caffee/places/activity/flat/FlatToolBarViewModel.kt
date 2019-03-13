package net.caffee.places.activity.flat

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class FlatToolBarViewModel (context: Application, repository: Repository)
    : BaseViewModel<FlatToolBarViewModel.Handler>(context, repository),
        LifecycleObserver {

    interface Handler : BaseHandler{

    }
}