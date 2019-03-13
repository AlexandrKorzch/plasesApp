package net.caffee.places.ui.reservation

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.R
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.extensions.getTimeDayMonthSimpleDateFormat
import net.caffee.places.extensions.isNotPastTime
import net.caffee.places.extensions.setCurrentTime
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.AddBookingBody
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.repo.remote.model.PlaceExtended
import net.caffee.places.util.SingleLiveEvent
import java.util.*

class ReservationViewModel(context: Application, repository: Repository)
    : BaseViewModel<ReservationViewModel.ViewModelHandler>(context, repository), LifecycleObserver {

    companion object {
        const val EVENT_ON_CHANGE_DATE = 0
        const val EVENT_ON_OPEN_DIALOG = 1
    }

    private var placeId: Long = -1L

    val hall = ObservableField<BaseCategory>()
    val image = ObservableField<String>("")
    private val date = ObservableField<String>()
    private val peopleCountString = ObservableField<String>()
    private val isSendQueryEnabled = ObservableField<Boolean>()
    private val events = SingleLiveEvent<Int>()
    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = getTimeDayMonthSimpleDateFormat()

    init {
        isSendQueryEnabled.set(false)
        date.set("")
        peopleCountString.set("")
    }

    fun date() = date

    fun peopleCountString() = peopleCountString

    fun isSendQueryEnabled(): ObservableField<Boolean> = isSendQueryEnabled

    fun events() = events

    fun calendar() = calendar

    fun getPlace(placeId: Long) {
        showProgress()
        addDisposable(repository.getPlace(placeId)
                .subscribe(Consumer { onGetPlace(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }

    fun bookingRequest() {
        showProgress()
        val addBookingBody
                = AddBookingBody(
                placeId,
                hall.get()?.id!!,
                peopleCountString.get()?.toInt()!!,
                calendar.get(Calendar.DATE),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE))

        addDisposable(repository.addBooking(addBookingBody)
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onAddedBooking() },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }


    private fun onGetPlace(place: PlaceExtended) {
        image.set(place.image)
    }

    private fun onAddedBooking() {
        openAddedDialog()
    }

    fun selectDate() {
        events.value = EVENT_ON_CHANGE_DATE
    }

    fun hallClick() {
        getHandler().openHallList(placeId)
    }

    fun openAddedDialog() {
        events.value = EVENT_ON_OPEN_DIALOG
    }

    fun updateDate() {
        if (!calendar.isNotPastTime()) {
            calendar.setCurrentTime()
        }
        date.set(dateFormat.format(calendar.timeInMillis))
    }

    fun changeSendQueryEnabled() {
        val isEnabled = peopleCountString.get()!!.isNotEmpty()
                && date.get()!!.isNotEmpty()
                && peopleCountString.get()!!.toInt() > 0
                && hall.get() != null
        isSendQueryEnabled.set(isEnabled)
    }

    fun showErrorMinPeopleCount() {
        if (peopleCountString.get()!!.isNotEmpty() && peopleCountString.get()!!.toInt() == 0) {
            getHandler().showToastError(
                    R.string.dialog_fragment_reservation_error_min_people_count)
        }
    }

    fun showErrorMinDate() {
        if (calendar.timeInMillis < Calendar.getInstance().timeInMillis) {
            getHandler().showToastError(R.string.dialog_fragment_reservation_error_min_date)
        }
    }

    fun setDirCategory(item: BaseCategory) {
        hall.set(item)
        changeSendQueryEnabled()
    }

    fun setPlaceId(placeId: Long) {
        this.placeId = placeId
    }

    interface ViewModelHandler : BaseHandler {
        fun showToastError(textResId: Int)
        fun openHallList(placeId: Long)
    }
}