package net.caffee.places.repo.remote.error

import io.reactivex.functions.Consumer
import net.caffee.places.architecture.BaseViewModel
import java.lang.ref.WeakReference

class ApiErrorHandler(viewModel: BaseViewModel<*>) : Consumer<Throwable> {

    private var weakViewModel: WeakReference<BaseViewModel<*>> = WeakReference(viewModel)

    override fun accept(t: Throwable?) {
        val ex = t as? ApiException
        ex?.let {
            weakViewModel.get()?.hideProgress()
            weakViewModel.get()?.showError(it.errorMessage)
        }
    }
}
