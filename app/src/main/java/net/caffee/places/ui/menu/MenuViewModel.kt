package net.caffee.places.ui.menu

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.GetProductsCategoriesBody
import net.caffee.places.repo.remote.model.ProdCategory
import net.caffee.places.repo.remote.model.ProdSubCategory

class MenuViewModel(context: Application, repository: Repository)
    : BaseViewModel<MenuViewModel.Handler>(context, repository),
        LifecycleObserver {

    private var placeId: Long = 0L

    private var list: List<ProdCategory> = ArrayList()

    val firstCategory = ObservableField<ProdCategory>()
    val secondCategory = ObservableField<ProdCategory>()
    val thirdCategory = ObservableField<ProdCategory>()

    val firstButtonPressed = ObservableBoolean(false)
    val secondButtonPressed = ObservableBoolean(false)
    val thirdButtonPressed = ObservableBoolean(false)

    private lateinit var firstList: List<ProdSubCategory>
    private lateinit var secondList: List<ProdSubCategory>
    private lateinit var thirdList: List<ProdSubCategory>

    val items = ObservableArrayList<ProdSubCategory>()

    fun loadData() {
        if (list.isEmpty()) {
            addDisposable(repository.getProductCategories(GetProductsCategoriesBody(placeId))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer { onGetProductCategories(it) },
                            ApiErrorHandler(this),
                            Action { hideProgress() })
            )
        }
    }

    private fun onGetProductCategories(list: List<ProdCategory>) {
        this.list = list
        initLists(list)
        onFirstBtClick()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if(!list.isEmpty()){
            when {
                firstButtonPressed.get() -> onFirstBtClick()
                secondButtonPressed.get() -> onSecondBtClick()
                thirdButtonPressed.get() -> onThirdBtClick()
            }
        }
    }

    private fun initLists(list: List<ProdCategory>) {
        firstCategory.set(list[0])
        secondCategory.set(list[1])
        thirdCategory.set(list[2])
        firstList = list[0].subcategories
        secondList = list[1].subcategories
        thirdList = list[2].subcategories
    }

    private fun showSubcategoryList(subcategories: List<ProdSubCategory>) {
        items.clear()
        items.addAll(subcategories)
    }

    fun onFirstBtClick() {
        showSubcategoryList(firstList)
        switchButtons(firstButtonPressed)
    }

    fun onSecondBtClick() {
        showSubcategoryList(secondList)
        switchButtons(secondButtonPressed)
    }

    fun onThirdBtClick() {
        showSubcategoryList(thirdList)
        switchButtons(thirdButtonPressed)
    }

    private fun switchButtons(press: ObservableBoolean) {
        firstButtonPressed.set(false)
        secondButtonPressed.set(false)
        thirdButtonPressed.set(false)
        press.set(true)
    }

    fun setPlaceId(placeId: Long) {
        this.placeId = placeId
    }

    interface Handler : BaseHandler {

    }
}