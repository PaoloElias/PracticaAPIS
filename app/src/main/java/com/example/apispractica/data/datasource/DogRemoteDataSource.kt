package com.example.apispractica.data.datasource

import com.example.apispractica.data.remote.DogApiService
import com.example.apispractica.data.remote.DogImageDto

interface DogRemoteDataSource {
    suspend fun getRandomDogImage(): DogImageDto
}

class DogRemoteDataSourceImpl(
    private val apiService: DogApiService
) : DogRemoteDataSource {
    override suspend fun getRandomDogImage(): DogImageDto {
        return apiService.getRandomDogImage()
    }
}
