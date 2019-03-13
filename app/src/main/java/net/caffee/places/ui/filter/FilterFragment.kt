package net.caffee.places.ui.filter

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.warkiz.widget.IndicatorSeekBar
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentFilterBinding

class FilterFragment
    : BaseFragment<FragmentFilterBinding, FilterViewModel>() {

    companion object {
        private const val TAG = "FilterFragment"
        var fragmentType: String? = null
        fun getInstance(listener: Listener): FilterFragment {
            val args = Bundle().apply { }
            return FilterFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_filter

    override fun viewModelClass() = FilterViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_filter_title))
        listener?.setMenuClickable(
                ContextCompat.getColor(context!!, R.color.colorBlack),
                getString(R.string.profile_ok),
                false
        )
        initSeekBar()
    }

    private fun initSeekBar() {
        binding.indicatorSeekBar.setOnSeekChangeListener(object : IndicatorSeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?, thumbPosOnTick: Int) {}
            override fun onSectionChanged(seekBar: IndicatorSeekBar?, thumbPosOnTick: Int, textBelowTick: String?,fromUserTouch: Boolean) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {}
            override fun onProgressChanged(seekBar: IndicatorSeekBar?, progress: Int, progressFloat: Float, fromUserTouch: Boolean) {
                viewModel().distance.set(progress.toString())
                viewModel().distanceInt = progress
            }
        })
    }

    override fun getViewModelHandler() = object : FilterViewModel.Handler {

        override fun setDistance(distanceInt: Int) {
            binding.indicatorSeekBar.setProgress(distanceInt.toFloat())
        }

        override fun changed(changed: Boolean) {
            this@FilterFragment.changed(changed)
        }

        override fun openChooser(type: String) {
            fragmentType = type
            listener?.setMenuClickable(ContextCompat.getColor(activity!!, R.color.colorTransparent), "", false)
            listener?.openChooser(type)
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

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openChooser(type: String)
        fun setMenuClickable(color: Int, text: String, clickable: Boolean)
    }
}
