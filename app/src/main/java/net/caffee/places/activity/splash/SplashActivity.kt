package net.caffee.places.activity.splash

import android.content.Intent
import android.os.Bundle
import net.caffee.places.R
import net.caffee.places.activity.common.CommonActivity
import net.caffee.places.activity.login.LoginActivity
import net.caffee.places.activity.main.MainActivity
import net.caffee.places.architecture.BaseActivity
import net.caffee.places.databinding.ActivitySplashBinding


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun layoutResId() = R.layout.activity_splash

    override fun viewModelClass() = SplashViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        viewModel().decidePage()
    }

    override fun getViewModelHandler() = object : SplashViewModel.Handler {
        override fun openLogin() = nextPage(LoginActivity.newIntent(this@SplashActivity))
        override fun openMain() = nextPage(MainActivity.newIntent(this@SplashActivity))
        override fun openAdvantages() = nextPage(CommonActivity.newIntent(this@SplashActivity,
                CommonActivity.TYPE_ADVANTAGES))
    }

    private fun nextPage(intent: Intent)
            = startActivity(intent.apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })

}



