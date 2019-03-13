package net.caffee.places.ui.minifilter

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.BottomSheetDialogMiniFilterBinding
import java.util.*

class MiniFilterDialog : BaseFragmentDialog<BottomSheetDialogMiniFilterBinding, MiniFilterViewModel>() {

    companion object {
        const val TAG = "MiniFilterDialog"

        fun getInstance(listener: Listener): MiniFilterDialog {
            val args = Bundle().apply { }
            return MiniFilterDialog().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.bottom_sheet_dialog_mini_filter

    override fun viewModelClass() = MiniFilterViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.setCancelable(true)
            dialog.window.setGravity(Gravity.BOTTOM)
        }
        setClickListeners()
    }

    private fun setClickListeners() {
        val clickListener = View.OnClickListener {
            saveToDbFilter()
            dismiss()
        }
        binding.hideImageView.setOnClickListener(clickListener)
        binding.ivConfirm.setOnClickListener(clickListener)
    }

    private fun saveToDbFilter() {
        if (viewModel().changed.get()) {
            viewModel().changed.set(false)
            viewModel().saveFilter()
            listener?.requestPlaces()
        }
    }

    override fun getViewModelHandler() = object : MiniFilterViewModel.Handler {
        override fun openDatePicker() {
            val calendar = viewModel().calendar
            val datePickerDialog = DatePickerDialog(
                    activity,
                    datePickerCallback,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }

    private val datePickerCallback =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val calendar = viewModel().calendar
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel().updateDateFromPicker()
            }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun requestPlaces()
    }

    interface ViewModelHandler : BaseHandler {
    }
}