package net.caffee.places.ui.complaints

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentComplaintsBinding
import net.caffee.places.extensions.onTextChanged
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.util.ABUSE

class ComplaintsFragment : BaseFragment<FragmentComplaintsBinding, ComplaintsViewModel>() {

    companion object {
        private const val TAG = "ComplaintsFragment"

        fun getInstance(listener: Listener, placeId: Long): ComplaintsFragment {
            val args = Bundle().apply { putLong(PLACE_ID, placeId) }
            return ComplaintsFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    private var placeId: Long = 0L
    var listener: Listener? = null
    private var categoryName = ABUSE

    override fun layoutResId() = R.layout.fragment_complaints

    override fun viewModelClass() = ComplaintsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    private fun getArgs() {
        placeId = arguments?.getLong(PLACE_ID)!!
        viewModel().setPlaceId(placeId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_complaints_title))
        setupUi()
    }

    private fun setupUi() {
        binding.descriptionCountEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.descriptionCountEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.descriptionCountEditText.onTextChanged { _, _, _, _ ->
            viewModel().changeSendQueryEnabled()
        }
    }

    private fun openComplaintAddedDialog() {
        ComplainDialog.getInstance()
                .show(fragmentManager, ComplainDialog.TAG)
    }

    private fun openCategoryList() {
        listener?.openCategoryList(categoryName, categoryListener)
    }

    private val categoryListener = object : RecyclerViewFragment.CategoryListener {
        override fun changeCategory(item: BaseCategory) {
            viewModel().complaint().set(item.title)
            viewModel().setCategoryId(item.id)
            viewModel().changeSendQueryEnabled()
        }
    }

    override fun getViewModelHandler() = object : ComplaintsViewModel.Handler {
        override fun openComplaintAddedDialog() = this@ComplaintsFragment.openComplaintAddedDialog()
        override fun openCategoryList() = this@ComplaintsFragment.openCategoryList()
        override fun onOpenKeyboard() {
            // TODO write code
        }
    }

    override fun clearFields() {
        listener = null
        categoryName = ""
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openCategoryList(
                categoryName: String,
                categoryListener: RecyclerViewFragment.CategoryListener
        )
    }
}