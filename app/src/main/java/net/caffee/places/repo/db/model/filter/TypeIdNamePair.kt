package net.caffee.places.repo.db.model.filter

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TypeIdNamePair: RealmObject() {
    @PrimaryKey var id: Long = -1
    var name: String? = null
}