package net.caffee.places.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import net.caffee.places.R
import net.caffee.places.activity.common.CommonActivity
import net.caffee.places.activity.main.MainActivity
import net.caffee.places.architecture.BaseActivity
import net.caffee.places.databinding.ActivityLoginBinding
import net.caffee.places.extensions.replaceFragment
import net.caffee.places.ui.login.ConfirmationFragment
import net.caffee.places.ui.login.SignInFragment

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginActivityViewModel>()
        , SmsAllowListener {

    companion object {
        const val TAG = "LoginActivity"
        private const val FRAGMENT_CONTAINER = R.id.fragmentContainer
        fun newIntent(context: Context?) = Intent(context, LoginActivity::class.java)
    }

    override fun layoutResId() = R.layout.activity_login

    override fun viewModelClass() = LoginActivityViewModel::class.java

    private var smsListening: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        openSignUpFragment()
        setupToolbar()
    }

    private fun setupToolbar() {
        setupToolbar(binding.toolbar, false, R.drawable.ic_arrow_back_black)
        binding.toolbar.setTitleTextColor(
                ContextCompat.getColor(this@LoginActivity, R.color.colorWhite))
        setStatusBarTranslucent(true)
        removeAppBarOutline()
        supportActionBar?.title = ""
    }

    private fun openSignUpFragment() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                SignInFragment.getInstance(signUpFragmentListener)
        )
    }

    private fun openConfirmationFragment(phone: String) {
        replaceFragment(
                FRAGMENT_CONTAINER,
                ConfirmationFragment.getInstance(confirmationFragmentListener, phone),
                true, "", true
        )
    }

    private val signUpFragmentListener = object : SignInFragment.Listener {
        override fun openAgreements() {
            startActivity(CommonActivity.newIntent(
                    this@LoginActivity, CommonActivity.TYPE_TERMS_AND_CONDITIONS))
        }

        override fun openConfirmationFragment(phone: String) = this@LoginActivity.openConfirmationFragment(phone)
    }

    private val confirmationFragmentListener = object : ConfirmationFragment.Listener {
        override fun openMain() {
            this@LoginActivity.openMain()
        }
    }

    private fun openMain() {
        startActivity(
                MainActivity.newIntent(this@LoginActivity)
                        .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        )
    }

    override fun getSmsPermission() = smsListening

    override fun getViewModelHandler() = object : LoginActivityViewModel.Handler{
        override fun setSmsPermission(allow: Boolean) {
            smsListening = allow
        }
    }

    override fun onBackPressed() {
        val count = fragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            fragmentManager.popBackStack()
        }
    }
}

interface SmsAllowListener{
    fun getSmsPermission():Boolean
}
