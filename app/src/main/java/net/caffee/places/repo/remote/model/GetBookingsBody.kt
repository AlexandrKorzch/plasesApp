package net.caffee.places.repo.remote.model

class GetBookingsBody(
        val type: Int,
        val offset: Int = 0,
        val limit: Int = 1000)