package com.example.apispractica.data.remote

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de imagen aleatoria de perro de https://dog.ceo/api/breeds/image/random
 */
data class DogImageDto(
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: String?
)
