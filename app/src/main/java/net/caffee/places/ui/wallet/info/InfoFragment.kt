package net.caffee.places.ui.wallet.info

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentInfoBinding

class InfoFragment: BaseFragment<FragmentInfoBinding, InfoViewModel>() {

    companion object {
        private const val TAG = "InfoFragment"
        fun getInstance(listener: Listener): InfoFragment {
            return InfoFragment().apply {
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_info

    override fun viewModelClass() = InfoViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_info_title))
        setupUi()
    }

    private fun setupUi() {
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = InfoAdapter(mutableListOf(), infoAdapterListener)
    }

    private val infoAdapterListener = object : InfoAdapter.Listener {
        override fun selectItem(item: String) {
            // TODO ("not implemented")
        }
    }

    override fun getViewModelHandler() = object : InfoViewModel.Handler {

    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
    }
}