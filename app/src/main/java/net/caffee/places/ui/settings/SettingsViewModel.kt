package net.caffee.places.ui.settings

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableField
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository

class SettingsViewModel (context: Application, repository: Repository)
    : BaseViewModel<SettingsViewModel.Handler>(context, repository),
        LifecycleObserver{

    val language =  ObservableField<String>()

    fun changeNotificationsStatus() {
    }

    fun changeLanguage() {
        getHandler().changeLanguage()
    }

    interface Handler : BaseHandler {
        fun changeLanguage()
    }
}