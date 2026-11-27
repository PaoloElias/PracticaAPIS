package com.example.apispractica.data

import com.example.apispractica.data.remote.DogImageDto
import com.example.apispractica.data.remote.SimpsonsCharacterDto
import com.example.apispractica.model.PersonajeConPerro
import java.util.Locale

/**
 * Mapeador encargado de tomar las características seleccionadas del DTO de Simpsons
 * y el DTO de Dog CEO para transformarlos en el Modelo de dominio [PersonajeConPerro].
 */
fun SimpsonsCharacterDto.toDomain(dogDto: DogImageDto?): PersonajeConPerro {
    // CDN oficial para las imágenes de los Simpsons en resolución 500px
    val cdnBaseUrl = "https://cdn.thesimpsonsapi.com/500"
    val fullPortraitUrl = if (!portraitPath.isNullOrBlank()) {
        if (portraitPath.startsWith("http")) {
            portraitPath
        } else {
            val path = if (portraitPath.startsWith("/")) portraitPath else "/$portraitPath"
            "$cdnBaseUrl$path"
        }
    } else {
        ""
    }

    val edadFormateada = age?.let { "$it años" } ?: "Edad desconocida"
    val frase = phrases?.firstOrNull() ?: "Sin frase registrada"
    val perroUrl = dogDto?.message ?: ""

    // Extraer y formatear la raza del perro desde la URL de la Dog API
    val raza = extraeRazaDeUrl(perroUrl)

    return PersonajeConPerro(
        id = this.id,
        nombre = this.name ?: "Sin Nombre",
        edad = edadFormateada,
        ocupacion = this.occupation ?: "No especificada",
        estado = this.status ?: "Desconocido",
        frasePrincipal = frase,
        imagenSimpsonsUrl = fullPortraitUrl,
        imagenPerroUrl = perroUrl,
        razaPerro = raza
    )
}

/**
 * Extrae la raza del perro a partir del formato de URL devuelto por Dog API:
 * https://images.dog.ceo/breeds/weimaraner/n02092339_6430.jpg -> "Weimaraner"
 */
private fun extraeRazaDeUrl(url: String): String {
    if (url.isBlank()) return "Desconocida"
    return try {
        val parts = url.split("/breeds/")
        if (parts.size > 1) {
            val breedPart = parts[1].split("/")[0]
            breedPart.replace("-", " ")
                .split(" ")
                .joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                }
        } else {
            "Mestizo"
        }
    } catch (_: Exception) {
        "Mestizo"
    }
}

/**
 * Mapea una lista de DTOs de los Simpsons y una lista de DTOs de perros
 * hacia la lista de modelos de dominio [PersonajeConPerro].
 */
fun List<SimpsonsCharacterDto>.toDomainList(dogDtos: List<DogImageDto>): List<PersonajeConPerro> {
    return this.mapIndexed { index, simpsonsDto ->
        val dogDto = dogDtos.getOrNull(index)
        simpsonsDto.toDomain(dogDto)
    }
}
