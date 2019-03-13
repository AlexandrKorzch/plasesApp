package net.caffee.places.ui.advantages.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import net.caffee.places.repo.db.model.AdvantageRealm
import net.caffee.places.ui.advantages.fragment.AdvantageItemFragment

class AdvantagesPagerAdapter(fragmentManager: FragmentManager?, private val list: List<AdvantageRealm>)
: FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return AdvantageItemFragment.getInstance(
                list[position].title,
                list[position].description,
                list[position].image,
                list[position].photoPath,
                position)
    }

    override fun getCount() = list.size
}