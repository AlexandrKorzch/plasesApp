package net.caffee.places.ui.invoice.bill

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class BillViewModel (context: Application, repository: Repository)
    : BaseViewModel<BillViewModel.Handler>(context, repository),
        LifecycleObserver {

    interface Handler : BaseHandler
}