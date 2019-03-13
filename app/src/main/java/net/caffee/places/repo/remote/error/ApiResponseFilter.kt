package net.caffee.places.repo.remote.error

import io.reactivex.functions.Predicate
import net.caffee.places.repo.remote.model.BaseDto

class ApiResponseFilter<T : BaseDto<*>> : Predicate<T> {
    override fun test(t: T): Boolean {
        if(t.errorCode == 0){
            return true
        }else{
            throw ApiException(t.status, t.errorCode, t.errorMessage)
        }
    }
}