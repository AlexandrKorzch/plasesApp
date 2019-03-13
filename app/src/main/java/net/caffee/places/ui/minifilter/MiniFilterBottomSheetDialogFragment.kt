//package net.vjet.myplace.ui.minifilter
//
//import android.arch.lifecycle.ViewModelProviders
//import android.os.Bundle
//import net.vjet.myplace.BR
//import net.vjet.myplace.R
//import net.vjet.myplace.architecture.BaseBottomSheetFragmentDialog
//import net.vjet.myplace.databinding.BottomSheetDialogMiniFilterBinding
//
//class MiniFilterBottomSheetDialogFragment
//    : BaseBottomSheetFragmentDialog<BottomSheetDialogMiniFilterBinding, MiniFilterViewModel>() {
//
//    companion object {
//        const val TAG = "MiniFilterBottomSheetDialogFragment"
//
//        fun getInstance(listener: Listener): MiniFilterBottomSheetDialogFragment {
//            val args = Bundle().apply { }
//            return MiniFilterBottomSheetDialogFragment().apply {
//                arguments = args
//                this.listener = listener
//            }
//        }
//    }
//
//    var listener: Listener? = null
//
//    override fun layoutResId() = R.layout.bottom_sheet_dialog_mini_filter
//
//    override fun bindingVariable() = BR.viewModel
//
//    override fun viewModel() = ViewModelProviders.of(this).get(MiniFilterViewModel::class.java)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        viewModel().setHandler(viewModelHandler)
//    }
//
//    private val viewModelHandler = object : MiniFilterViewModel.Handler {
//
//    }
//
//    interface Listener
//}