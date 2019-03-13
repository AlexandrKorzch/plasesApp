package net.caffee.places.ui.notification

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableField
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.repo.Repository
import java.util.concurrent.TimeUnit

class BookingNotificationViewModel(context: Application, repository: Repository)
    : BaseViewModel<BookingNotificationViewModel.Handler>(context, repository),
        LifecycleObserver {

    val isOpened = ObservableField<Boolean>(false)

    val selectedFifteen = ObservableField<Boolean>(false)
    val selectedThirty = ObservableField<Boolean>(false)
    val selectedFortyFive = ObservableField<Boolean>(false)
    val selectedOneHour = ObservableField<Boolean>(false)

    private val selectedTime = mapOf(
            R.id.tv_fifteen_min to selectedFifteen,
            R.id.tv_thirty_min to selectedThirty,
            R.id.tv_forty_five_min to selectedFortyFive,
            R.id.tv_forty_one_hour to selectedOneHour
    )

    fun changeState() {
        isOpened.set(!isOpened.get()!!)
    }

    fun lateClick(min: Int, v: View) {
        selectedTime[v.id]?.set(true)
        selectedTime.forEach { if (it.key != v.id) it.value.set(false) }
        lateRequest(min)
    }

    fun onPlaceClick()  = getHandler().onPlaceClick()

    fun declineClick() = declineRequest()

    private fun declineRequest(){
        android.os.Handler()
                .postDelayed({ getHandler().closeDialog() },
                        TimeUnit.SECONDS.toMillis(1))
    }

    private fun lateRequest(min: Int) {
        android.os.Handler()
                .postDelayed({ getHandler().closeDialog() },
                        TimeUnit.SECONDS.toMillis(1))
    }

    interface Handler : BaseHandler {
        fun onPlaceClick()
        fun closeDialog()
    }
}