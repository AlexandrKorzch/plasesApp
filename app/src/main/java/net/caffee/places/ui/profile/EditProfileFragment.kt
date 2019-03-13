package net.caffee.places.ui.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentEditProfileBinding
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.util.CITY


class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, EditProfileFragmentViewModel>() {

    companion object {
        private const val TAG = "ProfileFragment"
        fun getInstance(listener: Listener): EditProfileFragment {
            val args = Bundle().apply {}
            return EditProfileFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_edit_profile

    override fun viewModelClass() = EditProfileFragmentViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.profile_change_profile))
        listener?.setMenuClickable(
                ContextCompat.getColor(context!!, R.color.colorBlack),
                getString(R.string.profile_ok),
                false)
    }

    fun addAvatar(imagePath: String?, bitmap: Bitmap?) {
        bitmap?.let {binding.ivPortret.setImageBitmap(it)}
        imagePath?.let {viewModel().setImagePath(it)}
    }

    override fun getViewModelHandler() = object : EditProfileFragmentViewModel.Handler {
        override fun onBackPressed() {
            listener?.onBackPressed()
        }

        override fun changed(changed: Boolean) {
            this@EditProfileFragment.changed(changed)
        }

        override fun changePhoto() {
            listener?.choosePhoto()
        }

        override fun openCity() {
            listener?.openCity(CITY, categoryListener)
        }
    }

    private fun changed(changed: Boolean) {
        val color: Int
        val text: String
        if (changed) {
            color = R.color.colorGreen
            text = getString(R.string.profile_ok)
        } else {
            color = R.color.colorBlack
            text = getString(R.string.profile_ok)
        }
        listener?.setMenuClickable(ContextCompat.getColor(activity!!, color), text, changed)
    }

    private val categoryListener = object : RecyclerViewFragment.CategoryListener {
        override fun changeCategory(item: BaseCategory) {
            viewModel().setDirCategory(item)
        }
    }

    override fun clearFields() {
        listener = null
    }

    fun saveProfileClick() = viewModel().saveProfileClick()

    interface Listener {
        fun toolbarTitle(title: String)
        fun openCity(categoryName: String, categoryListener: RecyclerViewFragment.CategoryListener)
        fun okClick()
        fun choosePhoto()
        fun setMenuClickable(color: Int, text: String, clickable: Boolean)
        fun onBackPressed()
    }
}