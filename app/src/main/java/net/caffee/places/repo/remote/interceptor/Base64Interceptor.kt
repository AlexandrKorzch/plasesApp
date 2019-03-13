package net.caffee.places.repo.remote.interceptor

import android.util.Base64
import android.util.Log
import net.caffee.places.repo.remote.api.ApiSettings.ACCEPT
import okhttp3.*
import okio.Buffer
import okio.ByteString.decodeBase64
//import timber.log.Timber
import java.io.IOException
import java.lang.Long
import java.nio.charset.Charset


class Base64Interceptor(val identificator: String) : Interceptor {

    val TAG = "RETROFIT"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        log("originalRequest", originalRequest)
        var modifiedRequest: Request? = null
        if (originalRequest.headers().toString().contains(ACCEPT, true)) {
            modifiedRequest = originalRequest
            Log.d(TAG, "modifiedRequest = originalRequest")
        } else {
            modifiedRequest = getModifiedRequest(originalRequest)
            Log.d(TAG, "modifiedRequest != originalRequest")
        }
        log("modifiedRequest", modifiedRequest)
        val originalResponse = chain.proceed(modifiedRequest)
        log("originalResponse", originalResponse)
        if (originalResponse?.header("Content-Type").equals("application/json; charset=UTF-8")) {
            val modifiedResponse = decodeResponseFromBase64(originalResponse)
            log("modifiedResponse", modifiedResponse)
            return modifiedResponse
        } else {
            return originalResponse
        }
    }

    //request
    private fun getModifiedRequest(originalRequest: Request): Request? {
        return if (originalRequest.body() == null) {
            modifiedRequestCreator(originalRequest, originalRequest.body())
        } else {
            val modifiedRequestBody = getModifiedRequestBody(originalRequest)
            modifiedRequestCreator(originalRequest, modifiedRequestBody)
        }
    }

    private fun getModifiedRequestBody(originalRequest: Request): RequestBody? {
        val originalRequestBodyString = bodyToString(originalRequest.body())
        Log.d(TAG, "$identificator originalRequestBodyString - $originalRequestBodyString")
        val data = originalRequestBodyString.toString().toByteArray(Charset.forName("UTF-8"))
        val modifiedRequestBodyString = "{\"data\" : \"${Base64.encodeToString(data, Base64.DEFAULT)
                .replace("\n", "")}\"}"
        Log.d(TAG, "$identificator modifiedRequestBodyString - $modifiedRequestBodyString")
        return RequestBody.create(originalRequest.body()?.contentType(), modifiedRequestBodyString)
    }

    private fun modifiedRequestCreator(originalRequest: Request, body: RequestBody?): Request? {
        return originalRequest.newBuilder()
                .method(originalRequest.method(), body)
                .build()
    }

    private fun bodyToString(request: RequestBody?): String? {
        try {
            val buffer = Buffer()
            if (request != null) {
                request.writeTo(buffer)
            } else {
                return null
            }
            return buffer.readUtf8()
        } catch (e: IOException) {
            Log.d(TAG, "did not work")
            return null
        }
    }

    //response
    private fun decodeResponseFromBase64(originalResponse: Response): Response {
        val source = originalResponse.body()?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer()
        val contentType = originalResponse.body()?.contentType()
        var responseBodyString = buffer?.clone()?.readString(Charset.forName("UTF-8")) ?: ""
        Log.d(TAG, "$identificator originalResponseBodyString - $responseBodyString")
        responseBodyString = decodeBase64(responseBodyString)?.string(Charset.forName("UTF-8"))!!
        logLong(responseBodyString)
        val newBody = ResponseBody.create(contentType, responseBodyString ?: "")
        return originalResponse.newBuilder()
                .body(newBody)
                .build()
    }

    //log
    private fun log(tag: String, request: Request?) {
        Log.d(TAG, "$identificator $tag method - ${request?.method()}")
//        Log.d(TAG, "$identificator $tag body - ${request?.body()}")
        Log.d(TAG, "$identificator $tag headers - ${request?.headers()}")
    }

    private fun log(tag: String, response: Response?) {
        Log.d(TAG, "$identificator $tag headers - ${response?.headers()}")
    }


    private fun logLong(veryLongString: String) {
        val maxLogSize = 1000
        for (i in 0..veryLongString.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > veryLongString.length) veryLongString.length else end
            Log.i(TAG, veryLongString.substring(start, end))
        }
    }
}
