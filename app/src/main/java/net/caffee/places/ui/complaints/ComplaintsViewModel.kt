package net.caffee.places.ui.complaints

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableField
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.AbuseBody

class ComplaintsViewModel(context: Application, repository: Repository)
    : BaseViewModel<ComplaintsViewModel.Handler>(context, repository), LifecycleObserver {

    companion object {
        private const val DESCRIPTION_LENGTH_MIN = 10
        private const val DESCRIPTION_LENGTH_MAX = 500
    }

    private var placeId: Long? = null
    private var complainCategory: Long = 0

    private val isDescriptionVisible = ObservableField<Boolean>()
    private val isSendEnabled = ObservableField<Boolean>()

    private val complaint = ObservableField<String>()
    private val description = ObservableField<String>()

    init {
        // TODO fix !!
        complaint.set("")
        description.set("")
        isSendEnabled.set(false)
        isDescriptionVisible.set(false)
    }

    fun onOpenCategoriesClick() {
        getHandler().openCategoryList()
    }

    fun onSendComplaintClick() {
        val abuse = AbuseBody(
                placeId!!,
                complainCategory,
                description.get()!!)

        showProgress()
        addDisposable(
                repository.sendAbuse(abuse)
                        .subscribe(Consumer { onSendAbuse() },
                                ApiErrorHandler(this),
                                Action { hideProgress() }))
    }

    private fun onSendAbuse() {
        getHandler().openComplaintAddedDialog()
    }


    // TODO fix !!
    fun changeSendQueryEnabled() {
        val isEnabled = complaint.get()!!.isNotEmpty()
                && description.get()!!.isNotEmpty()
                && description.get()!!.length in DESCRIPTION_LENGTH_MIN..DESCRIPTION_LENGTH_MAX
        isSendEnabled.set(isEnabled)
    }

    fun isSendEnabled(): ObservableField<Boolean> = isSendEnabled

    fun isDescriptionVisible() = isDescriptionVisible

    fun setDescriptionVisible() {
        isDescriptionVisible.set(true)
        getHandler().onOpenKeyboard()
    }

    fun complaint() = complaint

    fun description() = description

    fun setPlaceId(placeId: Long) {
        this.placeId = placeId
    }

    fun setCategoryId(id: Long) {
        complainCategory = id
    }

    interface Handler : BaseHandler {
        fun openCategoryList()
        fun onOpenKeyboard()
        fun openComplaintAddedDialog()
    }

}