package net.caffee.places.ui.review

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.AddCommentBody

class ReviewViewModel(context: Application, repository: Repository)
    : BaseViewModel<ReviewViewModel.Handler>(context, repository), LifecycleObserver {

    private var placeId: Long = 0L

    val comment = ObservableField<String>()
    val isSendEnabled = ObservableBoolean(false)

    fun onTextChanged(text: CharSequence) {
        isSendEnabled.set(text.length> 10)
    }

    fun sendMessageClick() {
        showProgress()
        addDisposable(repository.addComment(AddCommentBody(placeId, comment.get()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { onSentMessage() },
                        ApiErrorHandler(this),
                        Action { hideProgress() })
        )
    }

    private fun onSentMessage() {
        getHandler().showCommentAddedDialog()
    }

    fun setPlaceId(placeId: Long) {
        this.placeId = placeId
    }

    interface Handler : BaseHandler {
        fun showCommentAddedDialog()
    }
}