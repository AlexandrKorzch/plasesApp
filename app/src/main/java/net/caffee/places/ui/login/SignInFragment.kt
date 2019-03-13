package net.caffee.places.ui.login

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<FragmentSignInBinding, SignInFragmentViewModel>() {

    companion object {
        private const val TAG = "PromotionFragment"
        fun getInstance(listener: Listener): SignInFragment {
            val args = Bundle().apply {}
            return SignInFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_sign_in

    override fun viewModelClass() = SignInFragmentViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAgreementsClick()
    }

    private fun setAgreementsClick() {
        binding.tvAgreement.setOnClickListener({ listener?.openAgreements() })
    }

    override fun getViewModelHandler() = object : SignInFragmentViewModel.Handler {
        override fun openConfirmation() {
            listener?.openConfirmationFragment(binding.etPhone.text.toString())
        }
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun openConfirmationFragment(phone: String)
        fun openAgreements()
    }
}