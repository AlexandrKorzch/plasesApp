package net.caffee.places.ui.complaints

import android.os.Bundle
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.databinding.DialogSupportBinding
import net.caffee.places.ui.support.dialog.SupportDialog

class ComplainDialog : BaseFragmentDialog<DialogSupportBinding, ComplaintDialogViewModel>() {

    companion object {
        const val TAG = "SupportDialog"

        fun getInstance(): SupportDialog {
            val args = Bundle().apply { }
            return SupportDialog().apply { arguments = args }
        }
    }

    override fun layoutResId() = R.layout.dialog_support //todo change for complaint - it's copy

    override fun viewModelClass() = ComplaintDialogViewModel::class.java

    override fun getViewModelHandler() = object : ComplaintDialogViewModel.Handler {
        override fun closeDialog() {
            dismiss()
        }
    }

    override fun clearFields() {

    }
}