package net.caffee.places.architecture

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.tbruyelle.rxpermissions2.RxPermissions
import net.caffee.places.BR
import net.caffee.places.R
import net.caffee.places.extensions.obtainViewModel
import net.caffee.places.extensions.toast
import net.caffee.places.util.AskPermission
import net.caffee.places.util.logWithTAG


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<BaseHandler>>
    : AppCompatDialogFragment() {

    protected lateinit var binding: T
    private lateinit var viewModel: V

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected abstract fun viewModelClass(): Class<V>

    protected abstract fun getViewModelHandler(): BaseHandler

    protected fun viewModel(): V = viewModel

    protected abstract fun clearFields()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity!!.obtainViewModel(viewModelClass())
        viewModel.setHandler(getViewModelHandler())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId(), container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        logWithTAG(tag)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        this.lifecycle.addObserver(viewModel)
        this.lifecycle.addObserver(viewModel.repository)
        subscribeToDataLoadingEvents()
        subscribeToShowErrorEvents()
        subscribeToAskPermissionEvents()
    }

    protected fun showDialog(title: String, message: String) {
        context?.let {
            val builder = AlertDialog.Builder(context!!)
            builder.setMessage(message).setTitle(title)
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun subscribeToDataLoadingEvents() {
        viewModel().dataLoadingEvent.observe(this, Observer {
            with(activity as? Progress) {
                when (it) {
                    true -> this?.showProgress()
                    false -> this?.hideProgress()
                    null -> logWithTAG("No Attached to Activity")
                }
            }
        })
    }

    private fun subscribeToShowErrorEvents() {
        viewModel().showErrorEvent.observe(this, Observer {
            it?.let { activity?.toast(it) } //todo show error
        })
    }

    private fun subscribeToAskPermissionEvents() {
        viewModel().askPermissionEvent.observe(this, Observer {
            val askPermission: AskPermission? = it
            val permissions: Array<String>? = it?.permissions
            permissions?.let {
                RxPermissions(activity!!)
                        .request(*permissions)
                        .subscribe { granted ->
                            if (!granted) activity?.toast("No permission")
                            askPermission?.function?.invoke(granted)
                        }
            }
        })
    }

    fun hideSoftKeyboard(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDetach() {
        super.onDetach()
        clearFields()
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_right)
    }
}