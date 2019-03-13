package net.caffee.places.ui.advantages.handler

import android.content.res.Resources
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TextView
import net.caffee.places.R
import net.caffee.places.databinding.FragmentAdvantagesBinding

class SwitcherHandler(binding: FragmentAdvantagesBinding, var listener: NextAction) {

    private var currentPosition: Int = 0
    private var res: Resources = binding.root.context.resources
    private var buttons: MutableList<TextView>
            = mutableListOf(binding.tvFirst, binding.tvSecond, binding.tvThird, binding.tvFourth)

    init {
        initViewPagerPageListener(binding)
        checkSelectedItem(currentPosition)
        initSwitchPageByClick(binding)
    }

    private fun initViewPagerPageListener(binding: FragmentAdvantagesBinding) {
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) = setPosition(position, binding)
        })
    }

    private fun setPosition(position: Int, binding: FragmentAdvantagesBinding) {
        currentPosition = position
        unCheckItems()
        checkSelectedItem(position)
        changeNextButton(position, binding)
    }

    private fun unCheckItems() {
        for (button in buttons) {
            button.background =
                    ResourcesCompat.getDrawable(res,
                            R.drawable.background_button_circle_mini_filter_gray, null)
            button.setTextColor(ResourcesCompat.getColor(res, R.color.colorGray, null))
            ViewCompat.setElevation(button, 0.0f)
        }
    }

    private fun checkSelectedItem(position: Int) {
        buttons[position].background =
                ResourcesCompat.getDrawable(res,
                        R.drawable.background_button_circle_black, null)
        ViewCompat.setElevation(buttons[position], 50.0f)
    }

    private fun changeNextButton(position: Int, binding: FragmentAdvantagesBinding) {
        if (position == 3) {
            binding.tvNext.setBackgroundColor(ResourcesCompat.getColor(res, R.color.colorGreen, null))
            binding.tvNext.text = res.getString(R.string.advantages_enter)
        } else {
            binding.tvNext.setBackgroundColor(ResourcesCompat.getColor(res, R.color.colorBlack, null))
            binding.tvNext.text = res.getString(R.string.advantages_next)
        }
    }

    private fun initSwitchPageByClick(binding: FragmentAdvantagesBinding) {
        val onClickListener = View.OnClickListener {
            binding.viewPager.setCurrentItem(buttons.indexOf(it), true)
        }
        for (button in buttons) {
            button.setOnClickListener(onClickListener)
        }
        binding.tvNext.setOnClickListener({
            if(currentPosition == 3){
                listener.openLogin()
            }else{
                binding.viewPager.currentItem = ++currentPosition
            }
        })
    }

    interface NextAction{
       fun openLogin()
    }
}