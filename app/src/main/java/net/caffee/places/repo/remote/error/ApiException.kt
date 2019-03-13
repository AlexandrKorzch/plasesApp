package net.caffee.places.repo.remote.error

class ApiException(
        var status : String? = null,
        var errorCode : Int? = -1,
        var errorMessage : String? = null
) : RuntimeException()