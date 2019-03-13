package net.caffee.places.activity.menutitle

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mvc.imagepicker.ImagePicker
import com.tbruyelle.rxpermissions2.RxPermissions
import net.caffee.places.R
import net.caffee.places.activity.common.CommonActivity
import net.caffee.places.architecture.BaseActivity
import net.caffee.places.databinding.ActivityCustomToolbarBinding
import net.caffee.places.extensions.replaceFragment
import net.caffee.places.extensions.toast
import net.caffee.places.ui.common.fragments.RecyclerViewFragment
import net.caffee.places.ui.filter.FilterFragment
import net.caffee.places.ui.profile.EditProfileFragment

class CustomToolBarActivity : BaseActivity<ActivityCustomToolbarBinding, CustomToolBarViewModel>() {

    companion object {
        const val TAG = "CustomToolBarActivity"
        private const val FRAGMENT_CONTAINER = R.id.fragmentContainer
        const val EDIT_PROFILE = 1
        const val FILTER = 2
        private const val EXTRA_SCREEN_TYPE =
                "net.vjet.myplace.activity.menutitle.CustomToolBarActivity.screenType"

        fun newIntent(context: Context?, screenType: Int) = Intent(context, CustomToolBarActivity::class.java).apply {
            putExtra(EXTRA_SCREEN_TYPE, screenType)
        }
    }

    override fun layoutResId() = R.layout.activity_custom_toolbar

    override fun viewModelClass() = CustomToolBarViewModel::class.java

    private var screenType = EDIT_PROFILE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extra()
        setupUi()
    }

    private fun extra() {
        screenType = intent.getIntExtra(EXTRA_SCREEN_TYPE, EDIT_PROFILE)
    }

    private fun setupUi() {
        setupToolbar(binding.toolbar, true, R.drawable.ic_arrow_back_black)
        binding.tvMenu.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag(CommonActivity.EDIT_PROFILE_FRAGMENT_TAG)
                    as? EditProfileFragment
            fragment?.saveProfileClick()
            if(fragment == null)onBackPressed() // FilterFragment
        }
        when (screenType) {
            EDIT_PROFILE -> openEditProfile()
            FILTER -> openFilter()
        }
    }

    private fun openFilter() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                FilterFragment.getInstance(filterFragmentListener)
        )
    }

    private fun openEditProfile() {
        replaceFragment(
                FRAGMENT_CONTAINER,
                EditProfileFragment.getInstance(editProfileFragmentListener),
                false, CommonActivity.EDIT_PROFILE_FRAGMENT_TAG
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CommonActivity.PICK_IMAGE_REQUEST_CODE) {
            val bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data)
            val selectedImage = ImagePicker.getImagePathFromResult(this, requestCode, resultCode, data)
            val fragment = supportFragmentManager.findFragmentByTag(CommonActivity.EDIT_PROFILE_FRAGMENT_TAG) as EditProfileFragment
            fragment.addAvatar(selectedImage, bitmap)
        }
    }

    private val editProfileFragmentListener = object : EditProfileFragment.Listener {
        override fun onBackPressed() = this@CustomToolBarActivity.onBackPressed()

        override fun setMenuClickable(color: Int, text: String, clickable: Boolean) {
            this@CustomToolBarActivity.setMenuClickable(color, text, clickable)
        }

        override fun choosePhoto() {
            RxPermissions(this@CustomToolBarActivity)
                    .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        if (granted) {
                            ImagePicker.pickImage(this@CustomToolBarActivity, "")
                        } else {
                            toast("Sorry No permission")
                        }
                    }
        }

        override fun okClick() = onBackPressed()

        override fun openCity(categoryName: String,
                              categoryListener: RecyclerViewFragment.CategoryListener) {
            replaceFragment(
                    FRAGMENT_CONTAINER,
                    RecyclerViewFragment.getInstance(
                            recyclerViewFragmentListener, categoryListener, -1,categoryName),
                    true, "", true
            )
        }

        override fun toolbarTitle(title: String) {
            binding.tvTitle.text = title
        }
    }

    private val filterFragmentListener = object : FilterFragment.Listener {
        override fun toolbarTitle(title: String) {
            binding.tvTitle.text = title
        }

        override fun setMenuClickable(color: Int, text: String, clickable: Boolean) {
            this@CustomToolBarActivity.setMenuClickable(color, text, clickable)
        }

        override fun openChooser(categoryName: String) {
            replaceFragment(
                    FRAGMENT_CONTAINER,
                    RecyclerViewFragment.getInstance(null, null, -1, categoryName),
                    true, "", true
            )
        }
    }

    private val recyclerViewFragmentListener = object : RecyclerViewFragment.Listener {
        override fun getComplaintText(item: String) = onBackPressed()
        override fun showNavigationDrawerToolbarButton() {// TODO("not implemented")
        }

        override fun showBackToolbarButton() {// TODO("not implemented")
        }
    }

    private fun setMenuClickable(color: Int, text: String, clickable: Boolean) {
        binding.tvMenu.setTextColor(color)
        binding.tvMenu.text = text
        binding.tvMenu.isClickable = clickable
    }


    override fun getViewModelHandler() = object : CustomToolBarViewModel.Handler {
    }
}
