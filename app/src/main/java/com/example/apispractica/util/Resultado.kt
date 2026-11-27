package com.example.apispractica.util

/**
 * Clase sellada genérica para representar estados de éxito, error o carga,
 * siguiendo el patrón usado en PokeApiEjemplo.
 */
sealed class Resultado<out T> {
    data class Exito<out T>(val data: T) : Resultado<T>()
    data class Error(val mensaje: String, val excepcion: Throwable? = null) : Resultado<Nothing>()
    data object Cargando : Resultado<Nothing>()
}
