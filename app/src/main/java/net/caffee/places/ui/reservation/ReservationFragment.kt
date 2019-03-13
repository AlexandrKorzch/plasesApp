package net.caffee.places.ui.reservation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_reservation.*
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentReservationBinding
import net.caffee.places.extensions.onTextChanged
import net.caffee.places.extensions.toast
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.ui.reservation.dialogs.ReservationDialog
import java.util.*

class ReservationFragment : BaseFragment<FragmentReservationBinding, ReservationViewModel>() {

    companion object {
        private const val TAG = "ReservationFragment"

        fun getInstance(listener: Listener, placeId: Long): ReservationFragment {
            val args = Bundle().apply {
                putLong(PLACE_ID, placeId)
            }
            return ReservationFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null
    private var placeId: Long = 0L

    override fun layoutResId() = R.layout.fragment_reservation

    override fun viewModelClass() = ReservationViewModel::class.java

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
        setupUi()
        viewModelEvents()
        viewModel().getPlace(placeId)
    }

    private fun setupUi() {
        listener?.toolbarTitle(getString(R.string.fragment_reservation_title))
        peopleCountEditText.onTextChanged { _, _, _, _ ->
            viewModel().changeSendQueryEnabled()
            viewModel().showErrorMinPeopleCount()
        }
    }

    private fun viewModelEvents() {
        viewModel().events().observe(this, android.arch.lifecycle.Observer { type ->
            when (type) {
                ReservationViewModel.EVENT_ON_CHANGE_DATE -> selectDate()
                ReservationViewModel.EVENT_ON_OPEN_DIALOG -> openReservationDialog()
            }
        })
    }

    private fun openReservationDialog() {
        ReservationDialog.getInstance()
                .show(fragmentManager, ReservationDialog.TAG)
    }

    private fun selectDate() {
        val calendar = viewModel().calendar()
        val datePickerDialog = DatePickerDialog(
                activity,
                datePickerCallback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    private fun selectTime() {
        val calendar = viewModel().calendar()
        TimePickerDialog(
                activity,
                timePickerCallback,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show()
    }

    override fun getViewModelHandler() = object : ReservationViewModel.ViewModelHandler {

        override fun openHallList(placeId: Long) {
            listener?.openHallList(placeId, categoryListener)
        }

        override fun showToastError(textResId: Int) {
            activity?.toast(textResId)
        }
    }

    private val datePickerCallback =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val calendar = viewModel().calendar()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                selectTime()
            }

    private val timePickerCallback =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val calendar = viewModel().calendar()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                viewModel().updateDate()
                viewModel().showErrorMinDate()
                viewModel().showErrorMinPeopleCount()
                viewModel().changeSendQueryEnabled()
            }

    private val categoryListener = object : RecyclerViewFragment.CategoryListener {
        override fun changeCategory(item: BaseCategory) {
            viewModel().setDirCategory(item)
        }
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openHallList(placeId: Long, categoryListener: RecyclerViewFragment.CategoryListener)
    }
}