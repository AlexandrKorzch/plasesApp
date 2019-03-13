package net.caffee.places.repo.remote.interceptor

import android.util.Log
import net.caffee.places.repo.sp.SharedPrefRepo
import okhttp3.Interceptor
import okhttp3.Response

class TokenAuthenticator(val identificator: String) : Interceptor {

    val TAG = "RETROFIT"

    override fun intercept(chain: Interceptor.Chain?): Response {
        val builder = chain?.request()?.newBuilder()
        builder?.header("Accept-language", "ru")//todo get real language
        SharedPrefRepo.getAuthKey()?.let { builder?.header("authkey", SharedPrefRepo.getAuthKey()) }
        val newRequest = builder?.build()
        Log.d(TAG, "$identificator Accept-language - " + newRequest?.header("Accept-language"))
        Log.d(TAG, "$identificator token - " + newRequest?.header("token"))
        Log.d(TAG, "$identificator authkey - " + newRequest?.header("authkey"))
        return chain?.proceed(newRequest)!!
    }
}