package net.caffee.places.ui.invoice.bill

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentBillBinding
import net.caffee.places.extensions.toast

class BillFragment: BaseFragment<FragmentBillBinding, BillViewModel>() {

    companion object {
        private const val TAG = "BillFragment"
//        const val BILL_KEY = "BILL_KEY"

        fun getInstance(listener: Listener): BillFragment {
//            val args = Bundle().apply {
////                putParcelable(BILL_KEY, item) todo put parcelable item
//            }
            return BillFragment().apply {
                this.listener = listener
//                arguments = args
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_bill

    override fun viewModelClass() = BillViewModel::class.java

//    var itemBill: Any? = "" //todo get parcelable item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        itemBill = arguments?.getString(BILL_KEY)//todo get parcelable item
//        logWithTAG("itemBill - $itemBill")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle("Счет в Sky Bar")//todo change text
        setupUi()
    }

    private fun setupUi() {
        with(binding){
            tvPayFromApp.setOnClickListener{listener?.openPayment()}
            tvPhysicalPayment.setOnClickListener{activity?.toast("Физическая оплата")}
        }
    }

    override fun getViewModelHandler() = object : BillViewModel.Handler {
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openPayment()
    }
}