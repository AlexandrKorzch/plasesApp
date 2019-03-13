package net.caffee.places.repo.db.model.order

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import net.caffee.places.repo.db.model.basket.Basket

open class OrderDb(@PrimaryKey var id : Long? = null, var basket : Basket? = null): RealmObject(){

    var name : String? = null
    var phone : String? = null
    var street : String? = null
    var address : String? = null
    var comment : String? = null
    var payType : String? = null
    var cityId : Long? = null

    fun copy() : OrderDb{
        return OrderDb(id, basket?.copy()).apply {
            this.name = name
            this.phone = phone
            this.cityId = cityId
            this.street = street
            this.address = address
            this.comment = comment
            this.payType = payType
        }
    }

    override fun toString(): String {
        return "OrderDb(id=$id, basket=$basket, name=$name, " +
                "phone=$phone, street=$street, address=$address, " +
                "comment=$comment, payType=$payType, cityId=$cityId)"
    }
}