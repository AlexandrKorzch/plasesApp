package net.caffee.places.repo.remote.model

class GetCommentsBody(
        val place: Long,
        val offset: Int,
        val limit: Int)
