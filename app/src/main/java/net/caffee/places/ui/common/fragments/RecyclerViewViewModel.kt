package net.caffee.places.ui.common.fragments

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.filter.KitchenIdNamePair
import net.caffee.places.repo.db.model.filter.TypeIdNamePair
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.repo.remote.model.City
import net.caffee.places.repo.remote.model.Hall
import net.caffee.places.repo.remote.model.Support
import net.caffee.places.util.CITY
import net.caffee.places.util.KITCHEN
import net.caffee.places.util.SORTSTYLE
import net.caffee.places.util.TYPE
import java.util.concurrent.TimeUnit

class RecyclerViewViewModel(context: Application, repository: Repository)
    : BaseViewModel<RecyclerViewViewModel.Handler>(context, repository), LifecycleObserver {

    private var currentCategory: String? = null

    fun getCities(city: String) {
        currentCategory = city
        addDisposable(repository.getCities()
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetCities(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetCities(list: List<City>) {
        getHandler().showCategories(list)
        addDisposable(repository.getFilter()
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getHandler().setCheckedItem(it.cityId ?: -1) })
    }

    fun getPlacesTypes(types: String) {
        currentCategory = types
        addDisposable(repository.getPlacesTypes()
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetPlacesTypes(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetPlacesTypes(types: List<BaseCategory>) {
        getHandler().showCategories(types)
        addDisposable(repository.getFilter()
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it.type.forEach { getHandler().setCheckedItem(it.id) } })
    }

    fun getKitchens(kitchens: String) {
        currentCategory = kitchens
        addDisposable(repository.getKitchens()
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetKitchens(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetKitchens(types: List<BaseCategory>) {
        getHandler().showCategories(types)
        addDisposable(repository.getFilter()
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it.kitchenType.forEach { getHandler().setCheckedItem(it.id) } })
    }

    fun getSorts(sortStyle: String) {
        currentCategory = sortStyle
        addDisposable(repository.getSorts()
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetSorts(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetSorts(types: List<BaseCategory>) {
        getHandler().showCategories(types)
        addDisposable(repository.getFilter()
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getHandler().setCheckedItem(it.sortId ?: -1) })
    }

    fun getSupportCategory() {
        addDisposable(repository.getSupportCategories()
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetSupportCategories(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetSupportCategories(support: Support) {
        getHandler().showCategories(support.categories)
    }

    fun getAbuseCategory() {
        addDisposable(repository.getAbuseCategories()
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetAbuseCategories(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetAbuseCategories(abusCategories: List<BaseCategory>) {
        getHandler().showCategories(abusCategories)
    }

    fun getHall(placeId: Long) {
        addDisposable(repository.getHalls(placeId)
                .doOnRequest { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetHalls(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetHalls(halls: List<Hall>) {
        getHandler().showCategories(halls)
    }

    fun saveCategory(item: BaseCategory) {
        when (currentCategory) {
            CITY -> setCity(item)
            TYPE -> setType(item)
            KITCHEN -> setKitchen(item)
            SORTSTYLE -> setSort(item)
        }
    }

    private fun setType(item: BaseCategory) {
        addDisposable(repository.getFilter().subscribe {
            val filter = it.copy()
            var exist: Boolean = false
            filter.type.forEach { if (it.id == item.id) exist = true }
            if (!exist) {
                val type = TypeIdNamePair()
                type.id = item.id
                type.name = item.title
                filter.type.add(type)
                repository.updateFilter(filter)
            }
        })
    }

    private fun setKitchen(item: BaseCategory) {
        addDisposable(repository.getFilter().subscribe {
            val filter = it.copy()
            var exist: Boolean = false
            filter.kitchenType.forEach { if (it.id == item.id) exist = true }
            if (!exist) {
                val type = KitchenIdNamePair()
                type.id = item.id
                type.name = item.title
                filter.kitchenType.add(type)
                repository.updateFilter(filter)
            }
        })
    }

    private fun setCity(item: BaseCategory) {
        addDisposable(repository.getFilter().subscribe {
            val filter = it.copy()
            filter.cityId = item.id
            filter.cityName = item.title
            repository.updateFilter(filter)
        })
    }

    private fun setSort(item: BaseCategory) {
        addDisposable(repository.getFilter().subscribe {
            val filter = it.copy()
            filter.sortId = item.id
            filter.sortName = item.title
            repository.updateFilter(filter)
        })
    }

    fun removeCategory(item: BaseCategory) {
        when (currentCategory) {
            TYPE -> repository.removeType(item.id)
            KITCHEN -> repository.removeKitchen(item.id)
        }
    }

    interface Handler : BaseHandler {
        fun showCategories(list: List<BaseCategory>?)
        fun setCheckedItem(id: Long)
    }
}


