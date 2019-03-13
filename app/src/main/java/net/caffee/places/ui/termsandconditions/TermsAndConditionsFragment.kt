package net.caffee.places.ui.termsandconditions

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.architecture.Progress
import net.caffee.places.databinding.FragmentTermsAndConditionsBinding


class TermsAndConditionsFragment
    : BaseFragment<FragmentTermsAndConditionsBinding, TermsAndConditionsViewModel>() {

    companion object {
        private const val TAG = "TermsAndConditionsFragment"

        fun getInstance(listener: Listener): TermsAndConditionsFragment {
            val args = Bundle().apply { }
            return TermsAndConditionsFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_terms_and_conditions

    override fun viewModelClass() = TermsAndConditionsViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_terms_and_conditions_title))
    }

    override fun getViewModelHandler() = object : TermsAndConditionsViewModel.Handler {
        override fun showTermsAndConditiions(url: String) {
            binding.wvTerms.webViewClient = webViewClient
            binding.wvTerms.loadUrl(url);
        }
    }

    internal var webViewClient: WebViewClient? = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            with(activity as? Progress) {this?.hideProgress() }
        }
    }

    override fun clearFields() {
        listener = null
        webViewClient = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
    }
}