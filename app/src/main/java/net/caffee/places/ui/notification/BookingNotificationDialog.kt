package net.caffee.places.ui.notification

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.activity.notification.CloseDialog
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.DialogBookingNotificationBinding
import net.caffee.places.extensions.toast

class BookingNotificationDialog
    : BaseFragmentDialog<DialogBookingNotificationBinding, BookingNotificationViewModel>() {

    companion object {
        const val TAG = "BookingNotificationDialog"
        fun getInstance(listener: Listener): BookingNotificationDialog {
            return BookingNotificationDialog().apply {
                this.listener = listener
            }
        }
    }

    override fun layoutResId() = R.layout.dialog_booking_notification

    override fun viewModelClass() = BookingNotificationViewModel::class.java

    var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                (activity as? CloseDialog)?.close()
            }
        }
    }

    override fun clearFields() {
        listener = null
    }

    override fun getViewModelHandler() = object : BookingNotificationViewModel.Handler {
        override fun closeDialog() {
            activity?.toast("Опаздываю или Отмена брони")
            (activity as? CloseDialog)?.close()
            dismiss()
        }

        override fun onPlaceClick() {
            dismiss()
            listener?.onPlaceClick(123L)//todo get rel id of booking
        }
    }

    interface Listener {
        fun onPlaceClick(bookingId: Long)
    }

    interface ViewModelHandler : BaseHandler {
    }
}