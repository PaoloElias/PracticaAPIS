package com.example.apispractica.model

/**
 * Modelo de dominio único que consolida las características elegidas de ambas APIs
 * (Los Simpsons API + Dog CEO API) después de ser procesadas por el mapeador.
 */
data class PersonajeConPerro(
    val id: Int,
    val nombre: String,
    val edad: String,
    val ocupacion: String,
    val estado: String,
    val frasePrincipal: String,
    val imagenSimpsonsUrl: String,
    val imagenPerroUrl: String,
    val razaPerro: String
) {
    /**
     * Título unificado que combina información del personaje de los Simpsons
     * y su acompañante canino de la Dog API.
     */
    val tituloDuo: String
        get() = "$nombre & Perro $razaPerro"
}
