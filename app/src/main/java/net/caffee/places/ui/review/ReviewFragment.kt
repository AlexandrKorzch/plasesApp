package net.caffee.places.ui.review

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentReviewBinding
import net.caffee.places.global.PLACE_ID
import net.caffee.places.ui.support.dialog.SupportDialog

class ReviewFragment : BaseFragment<FragmentReviewBinding, ReviewViewModel>() {


    companion object {
        private const val TAG = "ReviewFragment"

        fun getInstance(listener: Listener, placeId: Long): ReviewFragment {
            val args = Bundle().apply { putLong(PLACE_ID, placeId) }
            return ReviewFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    private var placeId: Long = 0L

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
        listener?.toolbarTitle(getString(R.string.review_add_review))
    }

    override fun layoutResId() = R.layout.fragment_review

    override fun viewModelClass() = ReviewViewModel::class.java

    private  var listener: Listener? = null

    override fun getViewModelHandler() = object : ReviewViewModel.Handler{
        override fun showCommentAddedDialog() = openSupportDialog()
    }

    private fun openSupportDialog() {
        ReviewDialog.getInstance()
                .show(fragmentManager, SupportDialog.TAG)
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener{
        fun toolbarTitle(title: String)
    }
}