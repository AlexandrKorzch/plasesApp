package net.caffee.places.ui.reservation.dialogs

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.DialogReservationBinding

class ReservationDialog
    : BaseFragmentDialog<DialogReservationBinding, ReservationDialogViewModel>() {

    companion object {
        const val TAG = "ReservationDialog"

        fun getInstance(): ReservationDialog {
            val args = Bundle().apply { }
            return ReservationDialog().apply { arguments = args }
        }
    }

    override fun layoutResId() = R.layout.dialog_reservation

    override fun viewModelClass() = ReservationDialogViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelEvents()
    }

    private fun viewModelEvents() {
        viewModel().events().observe(this, android.arch.lifecycle.Observer { type ->
            when (type) {
                ReservationDialogViewModel.EVENT_ON_CLOSE_DIALOG -> dismiss()
            }
        })
    }

    interface ViewModelHandler : BaseHandler

    override fun getViewModelHandler() = object:ReservationDialogViewModel.Handler{

    }

    override fun clearFields() {

    }
}