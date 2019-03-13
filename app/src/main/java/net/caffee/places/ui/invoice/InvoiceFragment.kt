package net.caffee.places.ui.invoice

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.FragmentInvoiceBinding
import net.caffee.places.extensions.toast

class InvoiceFragment :  BaseFragment<FragmentInvoiceBinding, InvoiceViewModel>() {

    companion object {
        private const val TAG = "InvoiceFragment"

        fun getInstance(listener: Listener): InvoiceFragment {
            val args = Bundle().apply { }
            return InvoiceFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_invoice

    override fun viewModelClass() = InvoiceViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.main_invoice))
        setupAdapter()

        //todo change this
        Handler().postDelayed({
            viewModel().getFullList()
        }, 100)
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = InvoiceAdapter(mutableListOf(), invoiceAdapterListener)
    }

    override fun clearFields() {
        listener = null
    }

    private val invoiceAdapterListener = object : InvoiceAdapter.Listener {
        override fun orderGot(bookingId: Long) {activity?.toast("Заказ получен") }
        override fun physicalPayment(bookingId: Long) {activity?.toast("Физическая оплата") }
        override fun getOrder(orderId: Long) {
            viewModel().getOrder(orderId)
        }
    }

    override fun getViewModelHandler() = object : InvoiceViewModel.Handler {
        override fun onGetRequestForOrder() {
            listener?.onGetRequestForOrder()
        }
    }

    interface Listener {
        fun onGetRequestForOrder()
        fun toolbarTitle(title: String)
    }

    interface Handler : BaseHandler
}
