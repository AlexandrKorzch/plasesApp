package net.caffee.places.ui.place.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import net.caffee.places.ui.place.fragment.GalleryItemFragment

class PlacePagerAdapter(fragmentManager: FragmentManager?, var list: MutableList<String>) :
        FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount() = list.size

    override fun getItem(position: Int): Fragment {
        return GalleryItemFragment.getInstance(list[position])
    }
}