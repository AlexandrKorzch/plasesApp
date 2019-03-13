package net.caffee.places.ui.places

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.v7.widget.SearchView
import io.reactivex.Flowable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.GetFilteredPlacesBody
import net.caffee.places.repo.remote.model.Place
import net.caffee.places.ui.places.PlacesFragment.Companion.FAVORITE_PLACES
import net.caffee.places.ui.places.filter.PlacesFilter
import net.caffee.places.util.SingleLiveEvent
import net.caffee.places.util.initFromDbFilter
import net.caffee.places.util.initFromType

class PlacesViewModel(context: Application, repository: Repository)
    : BaseViewModel<PlacesViewModel.Handler>(context, repository), LifecycleObserver {

    private var type: Int = -1

    val cartCount = SingleLiveEvent<Int>()
    val bookingCount = SingleLiveEvent<Int>()

    var searchText: String = ""

    var list = ArrayList<Place>()

    val items = ObservableArrayList<Place>()

    val listSubject: BehaviorSubject<List<Place>> = BehaviorSubject.create<List<Place>>()
    val bageVisible = ObservableBoolean(false)
    val place = ObservableField<Place>()

    val filter = PlacesFilter(list) {
        items.clear()
        items.addAll(it)
        listSubject.onNext(it)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        getBookings()
        getBaskets()
        getPlacesRequest(type)
    }

    private fun getBookings() {
        bookingCount.value = 0
    }

    private fun getBaskets() {
        addDisposable(repository.getGoodsCount()
                .subscribe { cartCount.value = it })
    }

    private fun getPlacesRequest(type: Int) {
        addDisposable(Flowable.just(GetFilteredPlacesBody())
                .flatMap({ Flowable.just(type) },
                        { filterBody, typeFrFl ->
                            initFromType(typeFrFl, filterBody)
                            filterBody
                        })
                .flatMap({ repository.getFilter() },
                        { filterBody, filterDb ->
                            initFromDbFilter(filterBody, filterDb)
                            filterBody
                        })
                .subscribe { getPlacesWithFilter(it) }
        )
    }

    private fun getPlacesWithFilter(filter: GetFilteredPlacesBody) {
        addDisposable(repository.getFilteredPlaces(filter)
                .doOnRequest {showProgress()}
                .doOnError { clearList() }
                .subscribe(Consumer { showPlaces(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }

    private fun showPlaces(places: List<Place>) {
        list.clear()
        list.addAll(places)
        items.clear()
        items.addAll(places)
        listSubject.onNext(list)
    }

    private fun clearList() {
        list.clear()
        items.clear()
        listSubject.onNext(listOf(Place().apply { id = 0 }))
        place.set(null)
        bageVisible.set(false)
    }

    fun setFavoritePlace(placeId: Long, favorite: Boolean) {
        showProgress()
        addDisposable(repository.setFavorite(placeId)
                .subscribe(Consumer { },
                        ApiErrorHandler(this),
                        Action { onSetFavorite(favorite) })
        )
    }

    private fun onSetFavorite(favorite: Boolean) {
        if (type == FAVORITE_PLACES && !favorite) {
            getPlacesRequest(type)
        } else {
            hideProgress()
        }
    }

    val searchChangeListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true
        override fun onQueryTextChange(newText: String?): Boolean {
            searchText = newText ?: ""
            filter.filter(newText)
            return true
        }
    }

    fun showPlaceBage(placeId: Long) = list.forEach {
        if (it.id == placeId) {
            place.set(it)
            bageVisible.set(true)
        }
    }

    fun setPlacesType(type: Int) {
        this.type = type
    }

    interface Handler : BaseHandler {
    }
}