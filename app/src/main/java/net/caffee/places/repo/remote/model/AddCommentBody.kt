package net.caffee.places.repo.remote.model

class AddCommentBody(
        val place: Long,
        val comment: String? = "")
