package com.example.capgg.network

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.coincap.io/v2/"

private val moshi = Moshi.Builder()
    .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface PriceApiService {
    @GET("assets/bitcoin")
    suspend fun getProperties(): response
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object PriceApi {
    val retrofitService: PriceApiService by lazy { retrofit.create(PriceApiService::class.java) }
}
