package net.caffee.places.util

import net.caffee.places.repo.db.model.basket.Basket
import net.caffee.places.repo.db.model.basket.Goods
import net.caffee.places.repo.remote.model.Product


fun addToBasketOrChangeIfExist(basket: Basket, product: Product?, count: Int?, placeName: String?,
                               placeImage: String?): Basket {

    val newBasket = basket.copy()

    newBasket.placeName = placeName
    newBasket.placeImage = placeImage

    val goods = Goods(product?.id, product?.title, product?.price, product?.categoryId, product?.image,count)
    var exist = false
    newBasket.goods.forEachIndexed<Goods?> { i, item ->
        if (item?.id == goods.id) {
            newBasket.goods[i] = goods
            exist = true
        }
    }
    if (!exist) {
        newBasket.goods.add(goods)
    }

    return newBasket
}

fun updateCountInGoods(item: Goods, list: List<Goods>) {
    var goodsForChange: Goods? = null
    list.forEachIndexed { i, goods -> if (goods.id == item.id) goodsForChange = goods }
    goodsForChange?.count = item.count
}

fun calculateTotalPrice(list: List<Goods>?) : Float {
    var totalPrice = 0F
    list?.forEach { totalPrice += (it.price!! * it.count!!) }
    return totalPrice
}

fun getUpdatedBasket(basket: Basket, list: List<Goods>): Basket {
    return basket.copy().apply {
        this.goods.clear()
        this.goods.addAll(list)
    }
}