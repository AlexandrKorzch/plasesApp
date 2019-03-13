package net.caffee.places.ui.promotions

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.FragmentPromotionsBinding
import net.caffee.places.repo.remote.model.ActionPojo
import net.caffee.places.ui.promotions.adapter.PromotionsAdapter

class PromotionsFragment : BaseFragment<FragmentPromotionsBinding, PromotionsViewModel>() {

    companion object {
        private const val TAG = "PromotionsFragment"

        fun getInstance(listener: Listener): PromotionsFragment {
            val args = Bundle().apply {}
            return PromotionsFragment().apply {
                arguments = args
                this.listener = listener
            }
        }
    }

    var listener: PromotionsFragment.Listener? = null

    override fun layoutResId() = R.layout.fragment_promotions

    override fun viewModelClass() = PromotionsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.toolbarTitle(getString(R.string.fragment_promotions_title))
        setupUi()
    }

    private fun setupUi() {
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = PromotionsAdapter(mutableListOf(), promotionsAdapterListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_promotions_fragment, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.searchItem)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        super.onCreateOptionsMenu(menu, inflater)
    }



    private val promotionsAdapterListener = object : PromotionsAdapter.Listener {
        override fun selectItem(item: ActionPojo) {
            listener?.openPromotion(item.id)
        }
    }

    override fun getViewModelHandler() = object : ViewModelHandler {

    }

    override fun clearFields() {
        listener = null
    }

    interface Listener {
        fun toolbarTitle(title: String)
        fun openPromotion(promotionId: Long)
    }

    interface ViewModelHandler : BaseHandler
}