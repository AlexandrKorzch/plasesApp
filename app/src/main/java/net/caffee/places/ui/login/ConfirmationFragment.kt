package net.caffee.places.ui.login

import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.provider.Telephony
import android.view.View
import net.caffee.places.R
import net.caffee.places.activity.login.SmsAllowListener
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentConfirmationBinding
import net.caffee.places.receiver.SmsReceiver

class ConfirmationFragment : BaseFragment<FragmentConfirmationBinding, ConfirmationFragmentViewModel>(){

    companion object {
        private const val TAG = "ConfirmationFragment"
        const val PHONE = "phone"
        fun getInstance(listener: Listener, phone : String): ConfirmationFragment {
            val args = Bundle().apply {
                putString(PHONE, phone)
            }
            return ConfirmationFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_confirmation

    override fun viewModelClass() = ConfirmationFragmentViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerSmsListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments.let {
            viewModel().phone.set(arguments?.getString(PHONE))
            binding.tvPhoneReplace.text =
                    binding.tvPhoneReplace.text.toString()
                            .replace("phone_replace",
                                    viewModel().phone.get()!!,
                                    false)
        }
    }

    private fun registerSmsListener() {
        if ((activity as? SmsAllowListener)?.getSmsPermission()!!) {
            val authReceiver = SmsReceiver()
            authReceiver.setListener(object : SmsReceiver.Listener{
                override fun onTextReceived(codeFromSms: String) {
                    activity?.unregisterReceiver(authReceiver)
                    showSMS(codeFromSms)
                }
            })
            activity?.registerReceiver(authReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        }
    }

    private fun showSMS(code: String) {
        binding.etCode.setText(code)
        Handler().postDelayed({binding.tvSubmit.performClick()}, 500)
    }

    override fun getViewModelHandler() = object : ConfirmationFragmentViewModel.Handler {
        override fun openMain() {
            listener?.openMain()
        }
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun openMain()
    }
}