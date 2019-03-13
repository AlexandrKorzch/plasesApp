package net.caffee.places.ui.advantages

import android.os.Bundle
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.databinding.FragmentAdvantagesBinding
import net.caffee.places.repo.db.model.AdvantageRealm
import net.caffee.places.ui.advantages.adapter.AdvantagesPageTransformer
import net.caffee.places.ui.advantages.adapter.AdvantagesPagerAdapter
import net.caffee.places.ui.advantages.handler.SwitcherHandler

class AdvantagesFragment : BaseFragment<FragmentAdvantagesBinding, AdvantagesViewModel>() {

    companion object {
        private const val TAG = "AdvantagesFragment"

        fun getInstance(listener: Listener): AdvantagesFragment {
            val args = Bundle().apply {}
            return AdvantagesFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: Listener? = null

    override fun layoutResId() = R.layout.fragment_advantages

    override fun viewModelClass() = AdvantagesViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle("")
    }

    override fun getViewModelHandler() = object : AdvantagesViewModel.Handler {
        override fun onGetData(list: List<AdvantageRealm>) {
            initViewPager(list)
        }

        override fun skip() {
            listener?.openLogin()
        }
    }

    private fun initViewPager(list: List<AdvantageRealm>) {
        binding.viewPager.adapter = AdvantagesPagerAdapter(fragmentManager, list)
        binding.viewPager.setPageTransformer(false, AdvantagesPageTransformer())
        SwitcherHandler(binding, object: SwitcherHandler.NextAction{
            override fun openLogin() {listener?.openLogin()}}
        )
    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openLogin()
    }
}