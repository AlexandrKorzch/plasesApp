package net.caffee.places.ui.notification

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.activity.notification.CloseDialog
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.DialogInvoiceBinding
import net.caffee.places.extensions.toast
import net.caffee.places.ui.invoice.InvoiceAdapter
import net.caffee.places.ui.invoice.InvoiceViewModel

class InvoiceDialog : BaseFragmentDialog<DialogInvoiceBinding, InvoiceViewModel>() {

    companion object {
        const val TAG = "InvoiceDialog"
        const val BOOKIN_ID = "BOOKING_ID"

        fun getInstance(listener: Listener, bookingId: Long): InvoiceDialog {
            val args = Bundle().apply {
                putLong(BOOKIN_ID, bookingId)
            }
            return InvoiceDialog().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    override fun layoutResId() = R.layout.dialog_invoice

    override fun viewModelClass() = InvoiceViewModel::class.java

    var listener: Listener? = null

    var bookingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog)
        bookingId = arguments?.getLong(BOOKIN_ID)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window.setGravity(Gravity.TOP)
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
        binding.ivClose.setOnClickListener { (activity as? CloseDialog)?.close() }
        setupAdapter()
        viewModel().getBookingList()
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = InvoiceAdapter(mutableListOf(), invoiceAdapterListener)
        (binding.recyclerView.adapter as InvoiceAdapter).openItem(bookingId)
    }

    private val invoiceAdapterListener = object : InvoiceAdapter.Listener {
        override fun orderGot(bookingId: Long) {activity?.toast("Заказ получен") }
        override fun physicalPayment(bookingId: Long) {activity?.toast("Физическая оплата") }
        override fun getOrder(orderId: Long) {viewModel().getOrder(orderId)}
    }

    override fun clearFields() {
        listener = null
    }

    override fun getViewModelHandler() = object : InvoiceViewModel.Handler {
        override fun onGetRequestForOrder() {
            dismiss()
            listener?.openOnGetOrder()
        }
    }

    interface Listener {
        fun openOnGetOrder()
    }

    interface ViewModelHandler : BaseHandler {
    }
}

