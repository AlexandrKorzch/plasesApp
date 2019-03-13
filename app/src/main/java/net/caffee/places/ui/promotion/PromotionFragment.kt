package net.caffee.places.ui.promotion

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentPromotionBinding
import net.caffee.places.extensions.toast

class PromotionFragment : BaseFragment<FragmentPromotionBinding, PromotionViewModel>() {

    companion object {
        private const val TAG = "PromotionFragment"
        const val PROMOTION_ID = "PROMOTION_ID"

        fun getInstance(listener: Listener, promotionId: Long): PromotionFragment {
            val args = Bundle().apply {
                putLong(PROMOTION_ID, promotionId)
            }
            return PromotionFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null
    private var promotionId = 0L

    override fun layoutResId() = R.layout.fragment_promotion

    override fun viewModelClass() = PromotionViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promotionId = arguments?.getLong(PROMOTION_ID)!!
        viewModel().getPromotion(promotionId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_promotion_title))
    }

    override fun getViewModelHandler() = object : PromotionViewModel.Handler {
        override fun nextPromotion() {
            // TODO remove toast
            activity?.toast("next")
        }

        override fun previousPromotion() {
            // TODO remove toast
            activity?.toast("previous")
        }

        override fun openComplaints() {
            listener?.openComplaintsFragment()
        }
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openComplaintsFragment()
    }
}