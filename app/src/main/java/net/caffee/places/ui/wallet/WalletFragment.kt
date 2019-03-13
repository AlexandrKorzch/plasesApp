package net.caffee.places.ui.wallet

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentWalletBinding
import net.caffee.places.util.logWithTAG


class WalletFragment : BaseFragment<FragmentWalletBinding, WalletViewModel>() {

    companion object {
        private const val TAG = "WalletFragment"

        fun getInstance(listener: Listener): WalletFragment {
            val args = Bundle().apply {}
            return WalletFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_wallet

    override fun viewModelClass() = WalletViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_wallet_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.infoItem -> {
//                val view = activity?.findViewById<View>(R.id.infoItem)
                listener?.openInfo()
                return true
            }
        }
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        viewModel().getPayments()
    }

    private fun setupListAdapter() {
        binding.rvPayments.adapter = WalletPaymentsAdapter(mutableListOf(), object : WalletPaymentsAdapter.Listener {
            override fun selectItem(item: String) {
                logWithTAG("WalletPaymentsAdapter in WalletFragment selectItem $item")
            }
        })
        binding.rvPayments.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.wallet_wallet)
    }

    override fun getViewModelHandler() = object : WalletViewModel.Handler {
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun openInfo()
    }
}