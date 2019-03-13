package net.caffee.places.ui.support

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentSupportBinding
import net.caffee.places.extensions.onTextChanged
import net.caffee.places.extensions.toast
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.ui.support.dialog.SupportDialog
import net.caffee.places.util.SUPPORT

class SupportFragment : BaseFragment<FragmentSupportBinding, SupportViewModel>() {

    companion object {
        private const val TAG = "SupportFragment"

        fun getInstance(listener: Listener): SupportFragment {
            val args = Bundle().apply { }
            return SupportFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null
    private var categoryName = SUPPORT

    override fun layoutResId() = R.layout.fragment_support

    override fun viewModelClass() = SupportViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_support_title))
        setupUi()
    }

    private fun setupUi() {
        binding.questionEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.questionEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.questionEditText.onTextChanged { _, _, _, _ ->
            viewModel().changeSendQueryEnabled()
        }
        binding.userFirstNameEditText.onTextChanged { _, _, _, _ ->
            viewModel().changeSendQueryEnabled()
        }
        binding.userLastNameEditText.onTextChanged { _, _, _, _ ->
            viewModel().changeSendQueryEnabled()
        }
        binding.userEmailEditText.onTextChanged { _, _, _, _ ->
            viewModel().changeSendQueryEnabled()
        }
    }

    private fun openCategoryList() {
        listener?.openCategoryList(categoryName, categoryListener)
    }

    private fun openSupportDialog() {
        SupportDialog.getInstance()
                .show(fragmentManager, SupportDialog.TAG)
    }

    private val categoryListener = object : RecyclerViewFragment.CategoryListener {
        override fun changeCategory(item: BaseCategory) {
            viewModel().setDirCategory(item)
            viewModel().changeSendQueryEnabled()
        }
    }

    override fun getViewModelHandler() = object : SupportViewModel.Handler {
        override fun onOpenCategoryList() {
            openCategoryList()
        }

        override fun onOpenKeyboard() {
            // TODO write code
        }

        override fun showToastError(textResId: Int) {
            activity?.toast(textResId)
        }

        override fun onSendMessage() {
            openSupportDialog()
        }
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openCategoryList(
                categoryName: String,
                categoryListener: RecyclerViewFragment.CategoryListener
        )
    }

    override fun clearFields() {
        listener = null
    }

}