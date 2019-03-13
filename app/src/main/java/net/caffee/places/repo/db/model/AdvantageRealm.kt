package net.caffee.places.repo.db.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class AdvantageRealm(
        var createdBy: String? = null,
        @PrimaryKey var id: String? = null,
        var title: String? = null,
        var weight: String? = null,
        var updatedAt: String? = null,
        var status: String? = null,
        var description: String? = null,
        var image: String? = null,
        var updated_by: String? = null,
        var createdAt: String? = null,
        var photoPath: String? = null
) : RealmObject()