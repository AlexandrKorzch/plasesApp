package net.caffee.places.ui.wallet.info

import android.databinding.ObservableField
import net.caffee.places.repo.remote.model.WalletInfo

class InfoItemViewModel(info: WalletInfo) {

    val isOpened = ObservableField<Boolean>(false)
    val titleObs = ObservableField<String>(info.title)
    val description = ObservableField<String>(info.description)
    val image = ObservableField<String>(info.image)

    fun changeState() {
        isOpened.set(!isOpened.get()!!)
    }

}