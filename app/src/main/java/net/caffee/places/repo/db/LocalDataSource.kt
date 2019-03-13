package net.caffee.places.repo.db

import io.reactivex.Flowable
import net.caffee.places.repo.db.model.AdvantageRealm
import net.caffee.places.repo.db.model.basket.Basket
import net.caffee.places.repo.db.model.filter.Filter
import net.caffee.places.repo.db.model.order.OrderDb
import net.caffee.places.repo.remote.model.Advantage

interface LocalDataSource {

    /*ADVANTAGES*/
    fun saveAdvantage(it: Advantage): AdvantageRealm

    fun getAdvantages(): Flowable<List<AdvantageRealm>>


    /*BASKETS*/
    fun addBasket(basket: Basket)

    fun getAllBaskets(): Flowable<MutableList<Basket>>

    fun getGoodsCount(): Flowable<Int>

    fun getGoodsCountInBasket(basketId: Long): Flowable<Int>

    fun getBasketByIdOrCreate(placeId: Long) : Flowable<Basket>

    fun getBasketByIdOrFirst(placeId: Long) : Flowable<Basket>

    /*FILTER*/
    fun updateFilter(filter: Filter)

    fun getFilter(): Flowable<Filter>

    fun removeType(id: Long)

    fun removeKitchen(id: Long)

    /*ORDER*/
    fun setNewOrder(order : OrderDb)

    fun getOrderById(id: Long) : Flowable<OrderDb>

}