package net.caffee.places.ui.invoice.paid

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.databinding.FragmentBillPaidBinding

class BillIsPaidFragment : Fragment() {

    companion object {
        private const val TAG = "BillIsPaidFragment"
        fun getInstance(listener: BillIsPaidFragment.Listener): BillIsPaidFragment {
            return BillIsPaidFragment().apply {
                this.listener = listener
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding
                = DataBindingUtil.inflate<FragmentBillPaidBinding>(inflater,
                R.layout.fragment_bill_paid, container, false)

        binding.tvClose.setOnClickListener{listener?.closeBillPaidPage()}
        return binding.root
    }

    var listener : BillIsPaidFragment.Listener? = null

    interface Listener{
        fun closeBillPaidPage()
    }
}