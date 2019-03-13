package net.caffee.places.ui.faq.adapter

import android.databinding.ObservableBoolean
import net.caffee.places.repo.remote.model.Fag

class FaqItem(val fag: Fag) {

    private val isOpened = ObservableBoolean(false)

    fun changeState() {
        isOpened.set(!isOpened.get())
    }

    fun isOpened() = isOpened
}