package net.caffee.places.activity.notification

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import net.caffee.places.extensions.toast
import net.caffee.places.notification.Notification
import net.caffee.places.ui.notification.BillDialog
import net.caffee.places.ui.notification.BookingNotificationDialog
import net.caffee.places.ui.notification.InvoiceDialog

class NotificationActivity : AppCompatActivity(), CloseDialog {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getExtra()
    }

    private fun getExtra() {
        intent.extras?.let {
            when (it[Notification.NOTIFY_ID] as? Int) {
                Notification.bookingNotifyID -> openBookingNotificationDialog()
                Notification.billNotifyID -> openBillDialog(it[Notification.BILL_ID] as Long)
            }
        }
    }

    private fun openBookingNotificationDialog() {
        BookingNotificationDialog.getInstance(bookingDialogListener)
                .show(supportFragmentManager, BookingNotificationDialog.TAG)
    }

    private fun openInvoiceDialogDialog(bookingId: Long) {
        InvoiceDialog.getInstance(invoiceDialogListener, bookingId)
                .show(supportFragmentManager, InvoiceDialog.TAG)
    }

    private fun openBillDialog(billId: Long) {
        BillDialog.getInstance(billDialogListener, billId)
                .show(supportFragmentManager, BillDialog.TAG)
    }

    private val bookingDialogListener = object : BookingNotificationDialog.Listener {
        override fun onPlaceClick(bookingId: Long) = openInvoiceDialogDialog(bookingId)
    }

    private val invoiceDialogListener = object : InvoiceDialog.Listener {
        override fun openOnGetOrder() {
            toast("Ожидайте нотификацию, когда счет будет готов - 3 секунды")
            Handler().postDelayed({Notification().showBillNotification(applicationContext,
                                        "Счет", "Бронь столика в Sky Bar",
                                        123) } , 3000)//todo get real billId
            onBackPressed()
        }
    }

    private val billDialogListener = object : BillDialog.Listener {
        override fun openPayment(bookingId: Long) {
            toast("Открыть платежи")
        }
    }

    override fun close() = onBackPressed()
}

interface CloseDialog {
    fun close()
}