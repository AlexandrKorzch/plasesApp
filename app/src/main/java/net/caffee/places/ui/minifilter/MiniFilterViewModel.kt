package net.caffee.places.ui.minifilter

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.R
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.extensions.getMiniFilterSimpleDateFormat
import net.caffee.places.global.TODAY
import net.caffee.places.global.TOMORROW
import net.caffee.places.repo.Repository
import net.caffee.places.repo.db.model.filter.Filter
import net.caffee.places.repo.db.model.filter.TypeIdNamePair
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.BaseCategory
import java.util.*

class MiniFilterViewModel(context: Application, repository: Repository)
    : BaseViewModel<MiniFilterViewModel.Handler>(context, repository), LifecycleObserver {

    val calendar: Calendar = Calendar.getInstance()
    val date = ObservableField<String>(context.getString(R.string.mini_filter_chose_date))
    private val dateFormat = getMiniFilterSimpleDateFormat()

    var changed = ObservableBoolean(false)

    var restaurant = ObservableBoolean(false) //2
    var cafe = ObservableBoolean(false) //3
    var pabBar = ObservableBoolean(false) //5
    var coffeeHouse = ObservableBoolean(false) //4
    var sushiBar = ObservableBoolean(false) //6
    var steakHouse = ObservableBoolean(false) //7
    var laundgBar = ObservableBoolean(false) //8
    var karaoke = ObservableBoolean(false) //9

    var today = ObservableBoolean(true)
    var tomorrow = ObservableBoolean(false)
    var customDate = ObservableBoolean(false)

    private lateinit var typesFromApi: List<BaseCategory>

    private lateinit var filter: Filter

    private var isDateFromPicker: Boolean = false

    init {
        getPlacesTypes()
    }

    private fun getPlacesTypes() {
        addDisposable(repository.getPlacesTypes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetPlacesTypes(it) },
                        ApiErrorHandler(this),
                        Action {}))
    }

    private fun onGetPlacesTypes(types: List<BaseCategory>) {
        typesFromApi = types
    }

    private val typeList = listOf(
            Triple(R.id.iv_restaurant, restaurant, "2"),
            Triple(R.id.iv_cafe, cafe, "3"),
            Triple(R.id.iv_bar, pabBar, "5"),
            Triple(R.id.iv_coffeeHouse, coffeeHouse, "4"),
            Triple(R.id.iv_sushi, sushiBar, "6"),
            Triple(R.id.iv_steak, steakHouse, "7"),
            Triple(R.id.iv_laundgBar, laundgBar, "8"),
            Triple(R.id.iv_karaoke, karaoke, "9")
    )

    private val dateTypeList = listOf(
            Triple(R.id.fl_today, today, "today"),
            Triple(R.id.fl_tomorrow, tomorrow, "tomorrow"),
            Triple(R.id.fl_custom_date, customDate, "customDate")
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        addDisposable(repository.getFilter().subscribe {
            this.filter = it.copy()
            checkTypeIfOneInFilter(it)
            checkDateFromDbFilter(it)
        })
    }

    private fun checkDateFromDbFilter(filter: Filter) {
        isDateFromPicker = false
        when(filter.discountDate){
            TODAY -> check(dateTypeList, R.id.fl_today)
            TOMORROW -> check(dateTypeList, R.id.fl_tomorrow)
            else -> {
                check(dateTypeList, R.id.fl_custom_date)
                calendar.timeInMillis = filter.discountDate?.toLong() ?: 0
                date.set( dateFormat.format(calendar.timeInMillis)
                        ?: getApplication<Application>().getString(R.string.mini_filter_chose_date))
                isDateFromPicker = true
            }
        }
    }

    private fun checkTypeIfOneInFilter(filter: Filter?) {
        var chosedOne = false
        if (filter?.type?.size == 1) {
            val id = filter.type.first()?.id.toString()
            typeList.forEach {
                if (it.third == id) {
                    check(typeList, it.first)
                    chosedOne = true
                }
            }
        }
        if (!chosedOne) uncheckAll()
    }

    private fun uncheckAll() {
        typeList.forEach { it.second.set(false) }
    }

    fun typeClick(v: View) {
        check(typeList, v.id)
        changed.set(true)
    }

    fun dateClick(v: View) {
        when(v.id){
            R.id.fl_custom_date -> getHandler().openDatePicker()
            R.id.fl_today, R.id.fl_tomorrow -> setTodayOrTomorrowDate(v.id)
        }
    }

    private fun setTodayOrTomorrowDate(id: Int){
        updateDate(getApplication<Application>().getString(R.string.mini_filter_chose_date), id)
        isDateFromPicker = false
    }

    fun updateDateFromPicker() {
        updateDate(dateFormat.format(calendar.timeInMillis), R.id.fl_custom_date)
        isDateFromPicker = true
    }

    private fun updateDate(dateStr: String, viewId: Int){
        date.set(dateStr)
        check(dateTypeList, viewId)
        changed.set(true)
        when(viewId){
            R.id.fl_today -> filter.discountDate = TODAY
            R.id.fl_tomorrow -> filter.discountDate = TOMORROW
            R.id.fl_custom_date -> filter.discountDate = calendar.timeInMillis.toString()
        }
    }

    private fun check(list: List<Triple<Int, ObservableBoolean, String>>, id: Int)
            = list.forEach { it.second.set(it.first == id) }

    fun saveFilter() {
        var id: Long = -1L
        typeList.forEach {
            if (it.second.get()) id = it.third.toLong()
        }
        if (id != -1L) {
            val newPair = TypeIdNamePair()
            newPair.id = id
            newPair.name = getNameOfTypeById(id)
            filter.type.clear()
            filter.type.add(newPair)
            repository.updateFilter(filter)
        }
    }

    private fun getNameOfTypeById(id: Long): String? {
        var name = ""
        typesFromApi.forEach {if (it.id == id) { name = it.title }}
        return name
    }

    interface Handler : BaseHandler {
        fun openDatePicker()
    }
}