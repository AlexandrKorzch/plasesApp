package net.caffee.places.ui.booking

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentBookingBinding
import net.caffee.places.extensions.toast


class BookingFragment : BaseFragment<FragmentBookingBinding, BookingViewModel>() {

    companion object {
        private const val TAG = "BookingFragment"
        private const val TAB_COUNT = 2
        const val CURRENT_BOOKING = 1
        const val PAST_BOOKING = 2

        fun getInstance(listener: Listener): BookingFragment {
            val args = Bundle().apply {}
            return BookingFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_booking

    override fun viewModelClass() = BookingViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.booking_booking)
        initTabsWithViewPager()
    }

    private fun initTabsWithViewPager() {
        binding.viewPager.adapter = TabPagerAdapter(childFragmentManager, TAB_COUNT)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                val tab = binding.tabLayout.getTabAt(position)
                tab?.select()
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.setCurrentItem(tab!!.position, true)
            }
        })
    }

    inner class TabPagerAdapter(
            fm: FragmentManager,
            private var tabCount: Int) :
            FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> BookingPastFragment.getInstance(bookPastListener, PAST_BOOKING)
                1 -> BookingCurrentFragment.getInstance(bookCurrentListener, CURRENT_BOOKING)
                else -> null
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }

    override fun clearFields() {
        listener = null
    }

    private val bookPastListener = object : BookingPastFragment.Listener {
        override fun selectItem(bookingId: Long) {
            activity?.toast("past bookingId - $bookingId")
        }
    }

    private val bookCurrentListener = object : BookingCurrentFragment.Listener {
        override fun selectItem(bookingId: Long) {
            activity?.toast("current bookingId - $bookingId")
        }
    }

    override fun getViewModelHandler() = object : BookingViewModel.Handler {
    }

    interface Listener {
    }
}