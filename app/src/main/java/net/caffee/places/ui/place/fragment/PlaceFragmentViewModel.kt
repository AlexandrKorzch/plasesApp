package net.caffee.places.ui.place.fragment

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.*
import net.caffee.places.util.SingleLiveEvent

class PlaceFragmentViewModel(context: Application, repository: Repository)
    : BaseViewModel<PlaceFragmentViewModel.Handler>(context, repository), LifecycleObserver {

    companion object {
        const val EVENT_ON_OPEN_MENU = 1
        const val EVENT_ON_OPEN_COMPLAINTS = 2
        const val TYPE_RESERVATION = 3
        const val TYPE_DELIVERY = 4
    }

    private val COMMENTS_LIMIT = 4
    private var placeId: Long = 0
    private var limit = COMMENTS_LIMIT

    val cartCount = SingleLiveEvent<Int>()
    val bookingCount = SingleLiveEvent<Int>()

    val placeObs = ObservableField<PlaceExtended>()
    val favoriteState = ObservableBoolean(false)
    val gallery = ObservableArrayList<String>()
    val productsObs = ObservableArrayList<Product>()
    val comentsObs = ObservableArrayList<Comment>()

    val placeSubject: BehaviorSubject<Place> = BehaviorSubject.create<Place>()

    val onOpenLiveData = MutableLiveData<Int>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        bookingCount.value = 0
        addDisposable(repository.getGoodsCount()
                .subscribe { cartCount.value = it })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        limit = comentsObs.size
        comentsObs.clear()
    }

    fun getData(placeId: Long) {
        this.placeId = placeId
        getPlace(placeId)
        getComments()
    }

    private fun getPlace(placeId: Long) {
        showProgress()
        addDisposable(repository.getPlace(placeId) //todo make current place in db
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetPlace(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }

    private fun getComments() {
        showProgress()
        addDisposable(repository.getComments(GetCommentsBody(placeId, comentsObs.size, limit))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetComments(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }

    fun favoriteClick() {
        showProgress()
        favoriteState.set(!favoriteState.get())
        addDisposable(repository.setFavorite(placeObs.get()?.id!!)
                .subscribe(Consumer { },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetComments(comments: List<Comment>) {
        if (comentsObs.size == 0) getHandler().showComments(comments)
        else getHandler().onAddComments(comments)
        comentsObs.addAll(comments)
        limit = COMMENTS_LIMIT
    }

    private fun onGetPlace(place: PlaceExtended) {
        getHandler().showToolbarTitle(place.title)
        placeObs.set(place)
        favoriteState.set(place.isFavorite)
        placeSubject.onNext(place)
        showGallery(place)
        showMenu(place.products)
    }

    private fun showMenu(products: List<Product>) {
        productsObs.clear()
        productsObs.addAll(products)
    }

    private fun showGallery(place: PlaceExtended) {
        gallery.clear()
        gallery.add(place.image)
        gallery.addAll(place.gallery)
        getHandler().setPagerIndicatorSize(gallery.size)
    }

    fun loadMoreReviewClick() = getComments()

    fun openMenuFragment() {
        onOpenLiveData.value = EVENT_ON_OPEN_MENU
    }

    fun openComplaintsFragment() {
        onOpenLiveData.value = EVENT_ON_OPEN_COMPLAINTS
    }

    fun openBooking() {
        onOpenLiveData.value = TYPE_RESERVATION
    }

    fun openReservation() {
        onOpenLiveData.value = TYPE_RESERVATION
    }

    fun openDelivery() {
        onOpenLiveData.value = TYPE_DELIVERY
    }

    interface Handler : BaseHandler {
        fun setPagerIndicatorSize(size: Int)
        fun showComments(comments: List<Comment>)
        fun onAddComments(comments: List<Comment>)
        fun showToolbarTitle(title: String)
    }
}