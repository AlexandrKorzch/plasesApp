package net.caffee.places.ui.review

import android.os.Bundle
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.databinding.DialogSupportBinding

class ReviewDialog : BaseFragmentDialog<DialogSupportBinding, ReviewDialogViewModel>() {

    companion object {
        const val TAG = "ReviewDialog"
        fun getInstance(): ReviewDialog {
            val args = Bundle().apply { }
            return ReviewDialog().apply { arguments = args }
        }
    }

    override fun layoutResId() = R.layout.dialog_review

    override fun viewModelClass() = ReviewDialogViewModel::class.java

    override fun getViewModelHandler() = object : ReviewDialogViewModel.Handler {
        override fun closeDialog() {
            dismiss()
        }
    }

    override fun clearFields() {

    }
}