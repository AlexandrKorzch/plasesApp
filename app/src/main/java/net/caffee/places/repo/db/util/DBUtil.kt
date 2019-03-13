package net.caffee.places.repo.db.util

import net.caffee.places.repo.db.model.AdvantageRealm
import net.caffee.places.repo.remote.model.Advantage

fun getRealmAdvantageFromAdvantage(advantage: Advantage) : AdvantageRealm {

    return AdvantageRealm(
            advantage.createdBy,
            advantage.id,
            advantage.title,
            advantage.weight,
            advantage.updatedAt,
            advantage.status,
            advantage.description,
            advantage.image,
            advantage.updated_by,
            advantage.createdAt,
            advantage.photo
    )
}