package net.caffee.places.ui.payments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentPaymentsBinding
import net.caffee.places.util.logWithTAG

class PaymentsFragment : BaseFragment<FragmentPaymentsBinding, PaymentsViewModel>() {

    companion object {
        private const val TAG = "PaymentsFragment"

        fun getInstance(listener: Listener): PaymentsFragment {
            val args = Bundle().apply {}
            return PaymentsFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_payments

    override fun viewModelClass() = PaymentsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        viewModel().getPayments()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_payments_fragment, menu)
        val searchView = menu?.findItem(R.id.searchItem)?.actionView as SearchView
        searchView.setOnQueryTextListener(viewModel().searchChangeListener)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupListAdapter() {
        binding.rvPayments.adapter = PaymentsAdapter(mutableListOf(), object : PaymentsAdapter.Listener {
            override fun selectItem(item: String) {
                logWithTAG("WalletPaymentsAdapter in WalletFragment selectItem $item")
            }
        })
        binding.rvPayments.layoutManager = LinearLayoutManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.payments_payments)
    }

    override fun getViewModelHandler() = object : PaymentsViewModel.Handler {
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
    }
}