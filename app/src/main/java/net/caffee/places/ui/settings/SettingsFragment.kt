package net.caffee.places.ui.settings

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    companion object {
        private const val TAG = "SettingsFragment"
        var languageStr: String = "Русский"
        fun getInstance(listener: Listener): SettingsFragment {
            val args = Bundle().apply {}
            return SettingsFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_settings

    override fun viewModelClass() = SettingsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_settings_title))
        //todo remove this logic
        viewModel().language.set(languageStr)
    }

    override fun getViewModelHandler() = object : SettingsViewModel.Handler {
        override fun changeLanguage() {
            listener?.changeLanguage()
        }
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun changeLanguage()
    }
}