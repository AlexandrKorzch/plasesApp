package net.caffee.places.ui.profile

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.Observable
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.repo.remote.model.EditProfileBody
import net.caffee.places.repo.remote.model.Profile
import net.caffee.places.util.createFileBody

class EditProfileFragmentViewModel(context: Application, repository: Repository)
    : BaseViewModel<EditProfileFragmentViewModel.Handler>(context, repository),
        LifecycleObserver {

    var name = ""
    var lastName = ""
    var city = ""
    var image = ""

    val userFirstName = ObservableField<String>("")
    val userLastName = ObservableField<String>("")
    val userCity = ObservableField<String>("")
    val imageObs = ObservableField<String>("")

    var cityId: Long = 0

    var map: Map<String, ObservableField<String>>? = null

    init {
        getProfile()
    }

    private val changedCallBack = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            checkDifferences()
        }
    }

    private fun getProfile() {
        showProgress()
        addDisposable(repository.getProfile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetProfile(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetProfile(profile: Profile) {
        cityId = profile.cityId
        initCheckFields(profile)
        initObsFields()
        initChangeListeners()
    }

    private fun initCheckFields(profile: Profile) {
        name = profile.firstName
        lastName = profile.lastName
        city = profile.cityTitle
        image = profile.image
        addPlaceHolders()
    }

    private fun initObsFields() {
        userFirstName.set(name)
        userLastName.set(lastName)
        userCity.set(city)
        imageObs.set(image)
    }

    private fun initChangeListeners() {
        userFirstName.addOnPropertyChangedCallback(changedCallBack)
        userLastName.addOnPropertyChangedCallback(changedCallBack)
        userCity.addOnPropertyChangedCallback(changedCallBack)
        imageObs.addOnPropertyChangedCallback(changedCallBack)
        map = mapOf(name to userFirstName, lastName to userLastName, city to userCity, image to imageObs)
    }

    private fun addPlaceHolders() {//todo remove this method
        if (name.isEmpty()) name = "add your name"
        if (lastName.isEmpty()) lastName = "add your last name"
        if (city.isEmpty()) city = "add your city"
    }

    private fun checkDifferences() {
        var changed = false
        map?.forEach { t, u -> if (t != u.get()) { changed = true } }
        getHandler().changed(changed)
    }

    fun userCityClick() {
        getHandler().openCity()
    }

    fun changePhotoClick() = getHandler().changePhoto()

    fun setDirCategory(item: BaseCategory) {
        userCity.set(item.title)
        cityId = item.id
    }

    fun setImagePath(imagePath: String) {
        imageObs.set(imagePath)
    }

    fun saveProfileClick() = updateProfile()

    private fun updateProfile() {
        showProgress()
        addDisposable(repository.editProfile(
                EditProfileBody(cityId, userFirstName.get()!!, userLastName.get()!!))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { uploadPhoto() },
                        ApiErrorHandler(this),
                        Action { })
        )
    }

    private fun uploadPhoto() {
        if (image != imageObs.get()) {
            imageObs.get()?.let {
                val body = createFileBody(it)
                addDisposable(repository.updateProfileImage(body)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer { onProfileEdited() },
                                ApiErrorHandler(this),
                                Action { hideProgress() }))}
        } else {
            onProfileEdited()
        }
    }

    private fun onProfileEdited() {
        getHandler().onBackPressed()
    }

    interface Handler : BaseHandler {
        fun openCity()
        fun changePhoto()
        fun changed(changed: Boolean)
        fun onBackPressed()
    }
}