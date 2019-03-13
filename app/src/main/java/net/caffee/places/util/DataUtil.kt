package net.caffee.places.util

import net.caffee.places.repo.remote.model.BaseDto
import net.caffee.places.repo.remote.model.Cart


fun getCart(it: BaseDto<List<Cart>>, placeId: Long): Cart {
    var cart = Cart()
    it.data?.forEach {
        if (placeId == it.placeId) cart = it
    }
    return cart
}