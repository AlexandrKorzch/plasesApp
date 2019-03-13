package net.caffee.places.ui.support.dialog

import android.os.Bundle
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.databinding.DialogSupportBinding

class SupportDialog
    : BaseFragmentDialog<DialogSupportBinding, SupportDialogViewModel>() {

    companion object {
        const val TAG = "SupportDialog"

        fun getInstance(): SupportDialog {
            val args = Bundle().apply { }
            return SupportDialog().apply { arguments = args }
        }
    }

    override fun layoutResId() = R.layout.dialog_support

    override fun viewModelClass() = SupportDialogViewModel::class.java

    override fun getViewModelHandler() = object : SupportDialogViewModel.Handler {
        override fun closeDialog() {
            dismiss()
        }
    }

    override fun clearFields() {
    }
}