package com.example.apispractica.data.remote

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta de lista de personajes de https://thesimpsonsapi.com/api/characters
 */
data class SimpsonsResponseDto(
    @SerializedName("count") val count: Int?,
    @SerializedName("pages") val pages: Int?,
    @SerializedName("results") val results: List<SimpsonsCharacterDto>?
)

/**
 * DTO individual para un personaje de los Simpsons
 */
data class SimpsonsCharacterDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("birthdate") val birthdate: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("occupation") val occupation: String?,
    @SerializedName("portrait_path") val portraitPath: String?,
    @SerializedName("phrases") val phrases: List<String>?,
    @SerializedName("status") val status: String?
)
