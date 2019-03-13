package net.caffee.places.ui.notification

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.activity.notification.CloseDialog
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.DialogBillBinding
import net.caffee.places.extensions.toast
import net.caffee.places.ui.invoice.bill.BillViewModel

class BillDialog : BaseFragmentDialog<DialogBillBinding, BillViewModel>() {

    companion object {
        const val TAG = "BillDialog"
        const val BILL_KEY = "BILL_KEY"
        fun getInstance(listener: Listener, billId: Long): BillDialog {
            val args = Bundle().apply {
                //putParcelable(BILL_KEY, bill) todo put parcelable item
            }
            return BillDialog().apply {
                this.listener = listener
            }
        }
    }

    override fun layoutResId() = R.layout.dialog_bill

    override fun viewModelClass() = BillViewModel::class.java

    var listener: Listener? = null

    //    var bill: Any? = "" //todo get parcelable item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog)
//   bill = arguments?.getString(BILL_KEY)//todo get parcelable item
//   logWithTAG("itemBill - $itemBill")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvDeclineBooking.setOnClickListener { activity?.toast("отменить бронь") }
            tvPay.setOnClickListener {
                listener?.openPayment(123L)//todo get real id of booking
                dismiss()
                (activity as? CloseDialog)?.close() //todo remove
            }
        }
    }

    override fun clearFields() {
        listener = null
    }

    override fun getViewModelHandler() = object : BillViewModel.Handler {
    }

    interface Listener {
       fun openPayment(bookingId: Long)
    }

    interface ViewModelHandler : BaseHandler {
    }
}