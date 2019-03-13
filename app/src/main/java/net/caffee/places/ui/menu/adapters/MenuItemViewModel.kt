package net.caffee.places.ui.menu.adapters
import net.caffee.places.repo.remote.model.ProdSubCategory

class MenuItemViewModel(
        private val item: ProdSubCategory,
        private val listener: MenuAdapter.Listener) {

    fun item() = item

    fun onOpenItem() {
        listener.selectItem(item)
    }
}