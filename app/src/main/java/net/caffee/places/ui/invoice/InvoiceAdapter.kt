package net.caffee.places.ui.invoice

import net.caffee.places.R
import net.caffee.places.databinding.ItemInvoiceBookingBinding
import net.caffee.places.databinding.ItemInvoiceDeliveryBinding
import net.caffee.places.view.baseview.BaseTwoHolderAdapter
import net.caffee.places.view.baseview.TwiceTestObject

class InvoiceAdapter(list: MutableList<TwiceTestObject>?, private val listener: Listener)
    : BaseTwoHolderAdapter<ItemInvoiceBookingBinding, ItemInvoiceDeliveryBinding, TwiceTestObject>(list) {

    var openedItemId: Long? = null

    override fun getFirstLayoutId() = R.layout.item_invoice_booking
    override fun getSecondLayoutId() = R.layout.item_invoice_delivery

    override fun getType(obj: TwiceTestObject) = obj.type

    private fun isOpened(item: TwiceTestObject) = item.text == "Бронь в Sky Bar" //todo make real logic

    override fun bindFirstTypeItem(binding: ItemInvoiceBookingBinding, item: TwiceTestObject) {
        with(binding) {
            binding.viewModel = InvoiceItemViewModel(item.text, isOpened(item))
            tvGetOrder.setOnClickListener{listener.getOrder(123)}//todo get real id
            tvPhysicalPayment.setOnClickListener{listener.physicalPayment(123)}//todo get real id
        }
    }

    override fun bindSecondTypeItem(binding: ItemInvoiceDeliveryBinding, item: TwiceTestObject) {
        with(binding){
            viewModel = InvoiceItemViewModel(item.text, isOpened(item))
            tvOrderGot.setOnClickListener{listener.orderGot(123)}//todo get real id
        }
    }

    fun openItem(bookingId: Long?) {
        openedItemId = bookingId
    }

    interface Listener {
        fun orderGot(bookingId: Long)
        fun physicalPayment(bookingId: Long)
        fun getOrder(orderId: Long)
    }
}