package com.example.apispractica.data.datasource

import com.example.apispractica.data.remote.SimpsonsApiService
import com.example.apispractica.data.remote.SimpsonsCharacterDto

interface SimpsonsRemoteDataSource {
    suspend fun getCharacters(page: Int = 1): List<SimpsonsCharacterDto>
}

class SimpsonsRemoteDataSourceImpl(
    private val apiService: SimpsonsApiService
) : SimpsonsRemoteDataSource {
    override suspend fun getCharacters(page: Int): List<SimpsonsCharacterDto> {
        val response = apiService.getCharacters(page)
        return response.results ?: emptyList()
    }
}
