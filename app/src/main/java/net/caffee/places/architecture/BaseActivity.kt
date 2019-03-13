package net.caffee.places.architecture

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.ViewOutlineProvider
import android.view.Window
import android.view.WindowManager
import com.tbruyelle.rxpermissions2.RxPermissions
import dmax.dialog.SpotsDialog
import net.caffee.places.BR
import net.caffee.places.R
import net.caffee.places.activity.main.MainActivity
import net.caffee.places.extensions.obtainViewModel
import net.caffee.places.extensions.toast
import net.caffee.places.util.AskPermission
import net.caffee.places.util.logWithTAG


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<BaseHandler>>
    : AppCompatActivity(), Progress {

    protected lateinit var binding: T
    private lateinit var viewModel: V

    var progress: AlertDialog? = null

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected abstract fun viewModelClass(): Class<V>

    protected fun viewModel(): V = viewModel

    var outlineProvider: ViewOutlineProvider? = null

    protected abstract fun getViewModelHandler(): BaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel(viewModelClass())
        viewModel.setHandler(getViewModelHandler())
        binding = DataBindingUtil.setContentView(this, layoutResId())
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        this.lifecycle.addObserver(viewModel)
        this.lifecycle.addObserver(viewModel.repository)
        subscribeToDataLoadingEvents()
        subscribeToShowErrorEvents()
        subscribeToAskPermissionEvents()
    }

    protected fun setupToolbar(toolbar: Toolbar, backBt: Boolean, backIconId: Int) {
        setSupportActionBar(toolbar)
        if (backBt) showBackToolbarButton(toolbar, backIconId)
    }

    private fun showBackToolbarButton(toolbar: Toolbar, backIconId: Int) {
        toolbar.setNavigationIcon(backIconId)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    protected fun removeAppBarOutline() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val appBar: AppBarLayout? = binding.root.findViewById(R.id.appBar)
            if (outlineProvider == null) outlineProvider = appBar?.outlineProvider
            appBar?.outlineProvider = null
        }
    }

    protected fun showAppBarOutline() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val appBar: AppBarLayout? = binding.root.findViewById(R.id.appBar)
            if (outlineProvider != null) appBar?.outlineProvider = outlineProvider
        }
    }

    protected fun setStatusBarTranslucent(makeTranslucent: Boolean) {
        if (makeTranslucent) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
//        overridePendingTranNsition(R.anim.slide_in_from_right, R.anim.slide_out_from_right)
    }

    private fun showAlertDialog(title: String?, message: String, obj: () -> Unit, buttonText: String?) {
        AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText) { _, _ -> obj.invoke() }
                .setNegativeButton("cancel") { dialog, _ -> dialog?.dismiss() }
                .create().show()
    }

    private fun subscribeToDataLoadingEvents() {
        viewModel().dataLoadingEvent.observe(this, Observer {
            when (it) {
                true -> showProgress()
                false -> hideProgress()
            }
        })
    }

    private fun subscribeToShowErrorEvents() {
        viewModel().showErrorEvent.observe(this, Observer {
            it?.let { toast(it) } //todo show error
        })
    }

    private fun subscribeToAskPermissionEvents() {
        viewModel().askPermissionEvent.observe(this, Observer {
            val askPermission: AskPermission? = it
            val permissions: Array<String>? = it?.permissions
            permissions?.let {
                RxPermissions(this)
                        .request(*permissions)
                        .subscribe { granted ->
                            if (!granted) toast("No permission")
                            askPermission?.function?.invoke(granted)
                        }
            }
        })
    }

    override fun showProgress() {
        if (progress == null) {
            progress = SpotsDialog(this)
            progress?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val wmlp = progress?.getWindow()!!.attributes
            wmlp.gravity = Gravity.TOP
            wmlp.y = 100
            progress?.show()
        }
    }

    override fun hideProgress() {
        progress?.hide()
        progress = null
    }

    override fun onBackPressed() {
        hideProgress()
        if (this is MainActivity && supportFragmentManager.backStackEntryCount == 0) {
            showAlertDialog(null, "Exit?", { super.onBackPressed() }, "exit")
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_left)
        }
    }

    override fun onResume() {
        super.onResume()
        logWithTAG(this::class.simpleName)
    }

    override fun onPause() {
        super.onPause()
        hideProgress()
    }
}

interface Progress {
    fun showProgress()
    fun hideProgress()
}