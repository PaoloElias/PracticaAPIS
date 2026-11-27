package com.example.apispractica.ui

import com.example.apispractica.model.PersonajeConPerro

/**
 * Representa los estados de la interfaz de usuario en el patrón MVVM,
 * incluyendo soporte para filtrado y selección del modelo unificado de ambas APIs.
 */
sealed interface PersonajeUiState {
    data object Cargando : PersonajeUiState
    data class Exito(
        val personajes: List<PersonajeConPerro>,
        val personajesFiltrados: List<PersonajeConPerro>,
        val searchQuery: String = "",
        val personajeSeleccionado: PersonajeConPerro? = null
    ) : PersonajeUiState
    data class Error(val mensaje: String) : PersonajeUiState
}
