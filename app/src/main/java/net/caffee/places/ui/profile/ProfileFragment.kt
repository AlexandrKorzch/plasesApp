package net.caffee.places.ui.profile

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentProfileBinding
import net.caffee.places.extensions.toast

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileFragmentViewModel>() {

    companion object {
        private const val TAG = "ProfileFragment"

        fun getInstance(listener: Listener): ProfileFragment {
            val args = Bundle().apply {}
            return ProfileFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_profile

    override fun viewModelClass() = ProfileFragmentViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.profile_profile))
    }

    override fun getViewModelHandler() = object : ProfileFragmentViewModel.Handler {
        override fun openPayment() {activity?.toast("Пополнить баланс")}
        override fun editProfile(){listener?.openEditProfile()}
        override fun openPayments(){listener?.openPayments()}
        override fun openBooking(){listener?.openBooking()}
        override fun openWallet() {listener?.openWallet()}
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun openEditProfile()
        fun toolbarTitle(title: String)
        fun openWallet()
        fun openPayments()
        fun openBooking()
    }
}