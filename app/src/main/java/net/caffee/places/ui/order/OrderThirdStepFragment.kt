package net.caffee.places.ui.order

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentOrderThirdStepBinding
import net.caffee.places.global.PLACE_ID


class OrderThirdStepFragment : BaseFragment<FragmentOrderThirdStepBinding, OrderThirdStepViewModel>() {

    companion object {
        private const val TAG = "OrderSecondStepFragment"

        fun getInstance(listener: Listener, placeId: Long): OrderThirdStepFragment {
            val args = Bundle().apply { putLong(PLACE_ID, placeId) }
            return OrderThirdStepFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    private var placeId: Long = 0L

    override fun layoutResId() = R.layout.fragment_order_third_step

    override fun viewModelClass() = OrderThirdStepViewModel::class.java

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
        listener?.toolbarTitle(getString(R.string.order_order_is_processed))
        listener?.overrideBackPressed()
        initAlertWithComment()
    }

    private fun initAlertWithComment() {
        binding.spComment.setOnClickListener({
            showDialog(getString(R.string.order_comment), "Хочу получить заказ сегодня в 21.45. Когда приедете - позвоните. Я выйду и заберу заказ.")
        })
    }

    override fun getViewModelHandler() = object : OrderThirdStepViewModel.Handler {
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun overrideBackPressed()
    }
}