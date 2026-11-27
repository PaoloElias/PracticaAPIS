package com.example.apispractica.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface SimpsonsApiService {
    @GET("characters")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): SimpsonsResponseDto
}
