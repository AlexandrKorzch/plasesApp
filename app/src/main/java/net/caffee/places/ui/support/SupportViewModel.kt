package net.caffee.places.ui.support

import android.app.Application
import android.arch.lifecycle.LifecycleObserver
import android.databinding.ObservableField
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import net.caffee.places.R
import net.caffee.places.architecture.BaseHandler
import net.caffee.places.architecture.BaseViewModel
import net.caffee.places.repo.Repository
import net.caffee.places.repo.remote.error.ApiErrorHandler
import net.caffee.places.repo.remote.model.BaseCategory
import net.caffee.places.repo.remote.model.SendToSupportBody

class SupportViewModel(context: Application, repository: Repository)
    : BaseViewModel<SupportViewModel.Handler>(context, repository),
        LifecycleObserver {

    companion object {
        private const val QUESTION_LENGTH_MIN = 10
        private const val QUESTION_LENGTH_MAX = 500
    }

    private val isQuestionVisible = ObservableField<Boolean>()
    private val isSendEnabled = ObservableField<Boolean>()
    private val userFirstName = ObservableField<String>()
    private val userLastName = ObservableField<String>()
    private val userEmail = ObservableField<String>()
    private val category = ObservableField<String>()
    private val question = ObservableField<String>()
    private var categoryId: Long = 0

    init {
        isSendEnabled.set(false)
        isQuestionVisible.set(false)
        userFirstName.set("")
        userLastName.set("")
        userEmail.set("")
        category.set("")
        question.set("")
    }

    fun onOpenCategories() {
        getHandler().onOpenCategoryList()
    }

    fun sendMessageClick() {
        if (isEmailValid()) {
            sendMessageRequest()
        } else {
            getHandler().showToastError(R.string.fragment_support_user_email_error)
        }
    }

    private fun sendMessageRequest() {
        val message = SendToSupportBody(categoryId, userEmail.get()!!, question.get()!!)
        showProgress()
        addDisposable(repository.sendSupportMessage(message)
                .subscribe(Consumer { onSendSupportMessage() },
                        ApiErrorHandler(this),
                        Action { hideProgress() }))
    }

    private fun onSendSupportMessage() {
        getHandler().onSendMessage()
    }

    // TODO fix !!
    fun changeSendQueryEnabled() {
        val isEnabled = userFirstName.get()!!.isNotEmpty()
                && userLastName.get()!!.isNotEmpty()
                && userEmail.get()!!.isNotEmpty()
                && category.get()!!.isNotEmpty()
                && question.get()!!.isNotEmpty()
                && question.get()!!.length in QUESTION_LENGTH_MIN..QUESTION_LENGTH_MAX
        isSendEnabled.set(isEnabled)
    }

    fun isSendEnabled(): ObservableField<Boolean> = isSendEnabled

    fun isQuestionVisible() = isQuestionVisible

    fun setQuestionVisible() {
        isQuestionVisible.set(true)
        getHandler().onOpenKeyboard()
    }

    fun userFirstName() = userFirstName

    fun userLastName() = userLastName

    fun userEmail() = userEmail

    fun question() = question

    fun category() = category

    private fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail.get()).matches()
    }

    fun setDirCategory(item: BaseCategory) {
        categoryId = item.id
        category().set(item.title)
    }

    interface Handler : BaseHandler {
        fun onOpenCategoryList()
        fun onOpenKeyboard()
        fun showToastError(textResId: Int)
        fun onSendMessage()
    }
}