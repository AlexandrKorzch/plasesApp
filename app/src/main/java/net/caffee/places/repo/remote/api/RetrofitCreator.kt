package net.caffee.places.repo.remote.api

import com.google.gson.GsonBuilder
import net.caffee.places.repo.remote.RemoteRepository
import net.caffee.places.repo.remote.interceptor.Base64Interceptor
import net.caffee.places.repo.remote.interceptor.TokenAuthenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitCreator {

    fun initRetrofit(): Retrofit {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS);
            addInterceptor(Base64Interceptor("Base64Interceptor"))
            addInterceptor(TokenAuthenticator("TokenAuthenticator"))
            networkInterceptors().add(logInterceptor)
        }

        return Retrofit.Builder()
                .baseUrl(ApiSettings.SERVER)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(createGsonConverter())
                .client(client.build())
                .build()
    }

    fun initServices(retrofit: Retrofit) {
        RemoteRepository.api = retrofit.create(Api::class.java)
    }

    private fun createGsonConverter(): GsonConverterFactory {
        val builder = GsonBuilder()
        return GsonConverterFactory.create(builder.create())
    }
}