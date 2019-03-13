package net.caffee.places.ui.faq

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.fragment_faq.*
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.FragmentFaqBinding
import net.caffee.places.databinding.PopupWindowFaqBinding
import net.caffee.places.extensions.initPopap
import net.caffee.places.repo.remote.model.Contact
import net.caffee.places.ui.faq.adapter.FaqAdapter

class FaqFragment : BaseFragment<FragmentFaqBinding, FaqViewModel>() {

    companion object {
        private const val TAG = "FaqFragment"

        fun getInstance(listener: Listener): FaqFragment {
            val args = Bundle().apply {}
            return FaqFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var email = ""
    var phone = ""
    var listener: Listener? = null
    private var popupWindow: PopupWindow? = null

    override fun layoutResId() = R.layout.fragment_faq

    override fun viewModelClass() = FaqViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_faq_title))
        setupUi()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_faq_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.helpItem -> {
                val view = activity?.findViewById<View>(R.id.helpItem)
                showPopupWindowListOrMap(view)
                return true
            }
        }
        return false
    }

    private fun setupUi() {
        setupAdapter()
    }

    private fun setupAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = FaqAdapter(viewModel().items.toMutableList())
    }

    private fun showPopupWindowListOrMap(view: View?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<PopupWindowFaqBinding>(
                inflater, R.layout.popup_window_faq, null, false)
        binding.viewModel = PopupFaqViewModel(email, phone)
        popupWindow = initPopap(PopupWindow(activity), binding, view)
    }

    override fun getViewModelHandler() = object : FaqViewModel.Handler {
        override fun setContact(contact: Contact?) {
            email = contact?.email ?: ""
            phone = contact?.phone ?: ""
        }
    }

    override fun clearFields() {
        listener = null
        popupWindow = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
    }

    interface ViewModelHandler : BaseHandler
}