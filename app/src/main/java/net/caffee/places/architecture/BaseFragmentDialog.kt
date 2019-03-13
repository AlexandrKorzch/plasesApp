package net.caffee.places.architecture

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.caffee.places.BR
import net.caffee.places.extensions.obtainViewModel
import net.caffee.places.util.logWithTAG

abstract class BaseFragmentDialog<T : ViewDataBinding, V : BaseViewModel<BaseHandler>>
    : DialogFragment() {

    protected lateinit var binding: T
    private lateinit var viewModel: V

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected abstract fun getViewModelHandler(): BaseHandler

    protected abstract fun viewModelClass(): Class<V>

    protected abstract fun clearFields()

    protected fun viewModel(): V = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =  activity!!.obtainViewModel(viewModelClass())
        viewModel.setHandler(getViewModelHandler())
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onResume() {
        super.onResume()
        logWithTAG(tag)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()

        this.lifecycle.addObserver(viewModel)
        this.lifecycle.addObserver(viewModel.repository)
    }

    override fun onDetach() {
        super.onDetach()
        clearFields()
    }

}