package net.caffee.places.ui.common.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import net.caffee.places.R
import net.caffee.places.architecture.BaseFragment
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.databinding.FragmentBaseRecyclerViewBinding
import net.caffee.places.global.PLACE_ID
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.ui.common.adapters.CheckableAdapter
import net.caffee.places.ui.common.adapters.CheckableItemViewModel
import net.caffee.places.util.*

class RecyclerViewFragment
    : BaseFragment<FragmentBaseRecyclerViewBinding, RecyclerViewViewModel>() {

    companion object {
        private const val TAG = "RecyclerViewFragment"
        private const val ARGS_CATEGORY_NAME = "categoryName"

        fun getInstance(
                listener: Listener?,
                categoryListener: CategoryListener?,
                placeId: Long = -1,
                categoryName: String = ""
        ): RecyclerViewFragment {
            val args = Bundle().apply {
                putString(ARGS_CATEGORY_NAME, categoryName)
                putLong(PLACE_ID, placeId)
            }
            return RecyclerViewFragment().apply {
                arguments = args
                this.listener = listener
                this.categoryListener = categoryListener
            }
        }
    }

    private var listener: Listener? = null
    private var placeId: Long = 0L
    private var categoryListener: CategoryListener? = null
    private var categoryName = ""
    private var oneChoice: Boolean = true
    private val itemViewModelList = ArrayList<CheckableItemViewModel>()

    override fun layoutResId() = R.layout.fragment_base_recycler_view

    override fun viewModelClass() = RecyclerViewViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener?.showBackToolbarButton()
        arguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request()
    }

    override fun onPause() {
        super.onPause()
        listener?.showNavigationDrawerToolbarButton()
    }

    private fun arguments() {
        categoryName = arguments?.getString(ARGS_CATEGORY_NAME) ?: ""
        placeId = arguments?.getLong(PLACE_ID)!!
    }

    private fun setupAdapter(list: MutableList<BaseCategory>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = CheckableAdapter(list, checkableAdapterListener, categoryName)
    }


    private fun request() {
        when (categoryName) {
            CITY -> {
                oneChoice = true
                viewModel().getCities(CITY)
            }

            TYPE -> {
                oneChoice = false
                viewModel().getPlacesTypes(TYPE)
            }

            KITCHEN -> {
                oneChoice = false
                viewModel().getKitchens(KITCHEN)
            }

            SORTSTYLE -> {
                oneChoice = true
                viewModel().getSorts(SORTSTYLE)
            }

            SUPPORT -> {
                oneChoice = true
                viewModel().getSupportCategory()
            }

            ABUSE -> {
                oneChoice = true
                viewModel().getAbuseCategory()
            }

            HALL -> {
                oneChoice = true
                viewModel().getHall(placeId)
            }

            LANGUAGE -> resources.getStringArray(R.array.languages).toMutableList()

            else -> logWithTAG("ELSE")
        }
    }

    private val checkableAdapterListener = object : CheckableAdapter.Listener {
        override fun addedCategory(viewModel: CheckableItemViewModel) {
            itemViewModelList.add(viewModel)
        }

        override fun selectItem(item: BaseCategory) {
            if (oneChoice) itemViewModelList.forEach { if (it.item() != item) it.isChecked.set(false) }
            viewModel().saveCategory(item)
            listener?.getComplaintText(item.title)
            categoryListener?.changeCategory(item)
        }

        override fun unSelectItem(item: BaseCategory) {
            viewModel().removeCategory(item)
        }
    }

    override fun getViewModelHandler() = object : RecyclerViewViewModel.Handler {

        override fun setCheckedItem(id: Long) {
            itemViewModelList.forEach { if (it.item().id == id) it.isChecked.set(true) }
        }

        override fun showCategories(list: List<BaseCategory>?) {
            list?.let{setupAdapter(list.toMutableList())}
        }
    }

    override fun clearFields() {
        listener = null
        categoryListener = null
        categoryName = ""
    }

    interface Listener {
        fun getComplaintText(item: String)
        fun showNavigationDrawerToolbarButton()
        fun showBackToolbarButton()
    }

    interface CategoryListener {
        fun changeCategory(item: BaseCategory)
    }

    interface Handler : BaseHandler
}