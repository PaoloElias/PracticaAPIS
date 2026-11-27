package com.example.apispractica.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    val simpsonsApiService: SimpsonsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://thesimpsonsapi.com/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SimpsonsApiService::class.java)
    }

    val dogApiService: DogApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApiService::class.java)
    }
}
