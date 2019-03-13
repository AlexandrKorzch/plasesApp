package net.caffee.places.ui.promotion

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.ActionPojo
import net.caffee.places.repo.remote.model.GetActionsBody

class PromotionViewModel(context: Application, repository: Repository)
    : BaseViewModel<PromotionViewModel.Handler>(context, repository),
        LifecycleObserver {

    private var promotionId: Long = 0
    private var currentIndex: Int = 0
    val actionObs = ObservableField<ActionPojo>()
    private var actionsList = ArrayList<ActionPojo>()

    fun getPromotion(promotionId: Long) {
        this.promotionId = promotionId
        showProgress()
        addDisposable(repository.getAction(promotionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetPromotion(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))

        addDisposable(repository.getActions(GetActionsBody())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onGetActions(it) },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onGetPromotion(action: ActionPojo) {
        actionObs.set(action)
    }

    private fun onGetActions(actions: List<ActionPojo>) {
        actionsList.clear()
        actionsList.addAll(actions)
        actionsList.forEachIndexed { index, action ->
            if (action.id == promotionId) currentIndex = index
        }
    }

    fun nextPromotion() {
        if (currentIndex != actionsList.size - 1) {
            actionObs.set(actionsList[++currentIndex])
        }
    }

    fun previousPromotion() {
        if (currentIndex != 0) {
            actionObs.set(actionsList[--currentIndex])
        }
    }

    fun openComplaints() {
        getHandler().openComplaints()
    }

    interface Handler : BaseHandler {
        fun nextPromotion()
        fun previousPromotion()
        fun openComplaints()
    }
}