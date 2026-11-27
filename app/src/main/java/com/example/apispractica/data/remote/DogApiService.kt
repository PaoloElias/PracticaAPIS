package com.example.apispractica.data.remote

import retrofit2.http.GET

interface DogApiService {
    @GET("breeds/image/random")
    suspend fun getRandomDogImage(): DogImageDto
}
