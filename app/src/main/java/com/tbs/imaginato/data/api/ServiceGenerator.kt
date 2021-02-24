package com.tbs.imaginato.data.api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ServiceGenerator {
    companion object Factory {
        fun <S> createService(serviceClass: Class<S>, context: Context): S {

            val url: String = ApiConstant.BASE_URL
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                builder.header(ApiConstant.KEY_CONTENT_TYPE, ApiConstant.CONTENT_TYPE)
                builder.header(ApiConstant.KEY_IMSI, ApiConstant.IMSI)
                builder.header(ApiConstant.KEY_IMEI, ApiConstant.IMEI)
                val request = builder.build()
                chain.proceed(request)
            }
            httpClient.connectTimeout(60000, TimeUnit.SECONDS)
            httpClient.readTimeout(60000, TimeUnit.SECONDS)
            val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = httpClient.addInterceptor(logging).build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .client(client)
                .build()

            return retrofit.create(serviceClass)
        }
    }
}

object ApiProvider {
    //User Login
    fun loginUser(context: Context): APIRepository {
        return APIRepository(
            ServiceGenerator.createService(
                APIServices::class.java,
                context
            )
        )
    }
}

