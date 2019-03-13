package net.caffee.places.ui.menuitem

import android.os.Bundle
import android.view.ViewGroup
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragmentDialog
import net.caffee.places.databinding.DialogMenuItemBinding
import net.caffee.places.global.PLACE_ID

class MenuItemDialog : BaseFragmentDialog<DialogMenuItemBinding, MenuItemViewModel>() {

    companion object {
        const val TAG = "MenuItemDialog"
        const val PRODUCT_ID = "PRODUCT_ID"

        fun getInstance(productId: Long, placeId: Long): MenuItemDialog {
            val args = Bundle().apply {
                putLong(PLACE_ID, placeId)
                putLong(PRODUCT_ID, productId)
            }
            return MenuItemDialog().apply { arguments = args }
        }
    }

    override fun layoutResId() = R.layout.dialog_menu_item

    override fun viewModelClass() = MenuItemViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    private fun getArgs() {
        val placeId = arguments?.getLong(PLACE_ID)!!
        val productId = arguments?.getLong(PRODUCT_ID)!!
        viewModel().setProductId(productId, placeId)
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun getViewModelHandler() = object : MenuItemViewModel.Handler {
        override fun closeFragmentDialog() {
            this@MenuItemDialog.dismiss()
        }
    }

    override fun clearFields() {

    }
}